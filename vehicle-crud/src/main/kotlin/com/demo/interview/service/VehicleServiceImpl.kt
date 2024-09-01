package com.demo.interview.service

import com.demo.interview.model.Option
import com.demo.interview.model.Vehicle
import org.springframework.stereotype.Component
import java.util.*


@Component
class VehicleServiceImpl : VehicleService {

    override fun getById(id: Int): Optional<Vehicle> {
        return Optional.of(Vehicle(
                id = 1,
                name = "twingo",
                price = 123,
                option = setOf(Option(id = 1, name = "CD player", price = 456))
        ))
    }
}