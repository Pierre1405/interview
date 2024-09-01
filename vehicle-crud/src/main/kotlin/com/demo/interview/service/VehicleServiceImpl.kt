package com.demo.interview.service

import com.demo.interview.dao.VehicleDao
import com.demo.interview.dto.OptionDto
import com.demo.interview.dto.VehicleDto
import org.springframework.stereotype.Component
import java.util.*
import com.demo.interview.entity.OptionEntity as OptionEntity
import com.demo.interview.entity.VehicleEntity as VehicleEntity


@Component
class VehicleServiceImpl(
        val vehicleDao: VehicleDao
) : VehicleService {

    override fun getById(id: Int): Optional<VehicleDto> =
            vehicleDao.findById(id).map(this::mapEntityToDto)

    override fun getByIdWithDiscount(id: Int, discount: Int): Optional<VehicleDto> =
            vehicleDao.findById(id).map(this::mapEntityToDto).map { vehicle: VehicleDto ->
                applyDiscountToMostExpensiveOption(vehicle, discount)
            }

    private fun mapEntityToDto(vehicleEntity: VehicleEntity): VehicleDto =
            VehicleDto(
                    id = vehicleEntity.id,
                    name = vehicleEntity.name,
                    price = vehicleEntity.price,
                    optionDto = vehicleEntity.optionEntities.map { optionEntity: OptionEntity ->
                        OptionDto(
                                id = optionEntity.id,
                                name = optionEntity.name,
                                price = optionEntity.price
                        )
                    }
            )

    private fun applyDiscountToMostExpensiveOption(vehicle: VehicleDto, discount: Int): VehicleDto =
            if (vehicle.optionDto.isEmpty()) {
                vehicle
            } else {
                val mostExpensiveOptionDto: OptionDto = vehicle.optionDto.maxBy { option -> option.price }
                val priceWithDiscount: Int = kotlin.math.floor(mostExpensiveOptionDto.price.toDouble() * (100 - discount) / 100).toInt()
                val mostExpensiveOptionIndex = vehicle.optionDto.indexOf(mostExpensiveOptionDto)
                val optionWithDiscount = vehicle.optionDto.toMutableList()
                optionWithDiscount[mostExpensiveOptionIndex] = vehicle.optionDto[mostExpensiveOptionIndex].copy(
                        price = priceWithDiscount
                )
                vehicle.copy(
                        optionDto = optionWithDiscount.toList()
                )
            }

}