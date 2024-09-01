package com.demo.interview.controler

import com.demo.interview.model.Vehicle
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
class VehicleController {
    @GetMapping(path = ["/vehicle"])
    fun getVehicle(): Vehicle {
        return Vehicle(name = "twingo")
    }
}