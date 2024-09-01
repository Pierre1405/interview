package com.demo.interview.service

import com.demo.interview.dto.VehicleDto
import java.util.*

interface VehicleService {
    fun getById(id: Int): Optional<VehicleDto>
    fun getByIdWithDiscount(id: Int, discount: Int): Optional<VehicleDto>
}