package com.demo.interview.service

import com.demo.interview.model.Vehicle
import java.util.*

interface VehicleService {
    fun getById(id: Int): Optional<Vehicle>
}