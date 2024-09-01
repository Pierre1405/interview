package com.demo.interview.controler

import com.demo.interview.model.Vehicle
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
    fun getVehicle(@PathVariable id: Int): Vehicle {
        if (id <= 0) {
            throw IllegalArgumentException("Invalid vehicle ID")
        }
        return vehicleService
                .getById(id)
                .orElseThrow { EntityNotFoundException("Vehicle not found") }
    }
}