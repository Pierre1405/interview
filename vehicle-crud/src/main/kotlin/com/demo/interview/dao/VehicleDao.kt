package com.demo.interview.dao

import com.demo.interview.entity.Vehicle
import org.springframework.data.jpa.repository.JpaRepository

interface VehicleDao : JpaRepository<Vehicle, Int> {
}