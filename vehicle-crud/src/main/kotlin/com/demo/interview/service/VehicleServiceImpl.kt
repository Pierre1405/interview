package com.demo.interview.service

import com.demo.interview.dao.VehicleDao
import com.demo.interview.dto.Option
import com.demo.interview.dto.Vehicle
import org.springframework.stereotype.Component
import java.util.*
import com.demo.interview.entity.Vehicle as VehicleEntity
import com.demo.interview.entity.Option as OptionEntity


@Component
class VehicleServiceImpl(
        val vehicleDao: VehicleDao
) : VehicleService {


    override fun getById(id: Int): Optional<Vehicle> {
        val maybeVehicleEntity: Optional<VehicleEntity> = vehicleDao.findById(id);
        return maybeVehicleEntity.map { vehicleEntity ->
            Vehicle(
                id = vehicleEntity.id,
                name = vehicleEntity.name,
                price = vehicleEntity.price,
                option = vehicleEntity.options.map { optionEntity: OptionEntity ->
                    Option(
                            id = optionEntity.id,
                            name = optionEntity.name,
                            price = optionEntity.price
                    )
                }
            )
        }
    }
}