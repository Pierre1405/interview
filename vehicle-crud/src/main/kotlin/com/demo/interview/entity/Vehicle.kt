package com.demo.interview.entity

import jakarta.persistence.*

@Entity
data class Vehicle(
        @Id
        @GeneratedValue
        @Column(name = "vehicle_id")
        val id: Int,
        val name: String,
        val price: Int,
        @OneToMany()
        @JoinColumn(name = "vehicle_id")
        val options: List<Option>
)