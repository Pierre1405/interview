package com.demo.interview.controler

import com.demo.interview.dto.VehicleDto
import com.demo.interview.service.VehicleService
import jakarta.persistence.EntityNotFoundException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController()
class VehicleController(
        val vehicleService: VehicleService
) {

    @GetMapping(path = ["/vehicle/{id}"])
    fun getVehicle(@PathVariable id: Int): VehicleDto {
        if (id <= 0) {
            throw IllegalArgumentException("Invalid vehicle ID")
        }
        return vehicleService
                .getById(id)
                .orElseThrow { EntityNotFoundException("Vehicle not found") }
    }

    @GetMapping(path = ["/vehicle/{id}/discount/{discount}"])
    fun getVehicleDiscount(@PathVariable id: Int, @PathVariable discount: Int): VehicleDto {
        if (id <= 0) {
            throw IllegalArgumentException("Invalid vehicle ID")
        }
        return vehicleService
                .getByIdWithDiscount(id, discount)
                .orElseThrow { EntityNotFoundException("Vehicle not found") }
    }
}