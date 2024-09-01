package com.demo.interview.dao

import com.demo.interview.entity.VehicleEntity
import org.springframework.data.jpa.repository.JpaRepository

interface VehicleDao : JpaRepository<VehicleEntity, Int> {
}