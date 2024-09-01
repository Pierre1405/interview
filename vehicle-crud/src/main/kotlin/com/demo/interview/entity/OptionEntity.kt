package com.demo.interview.entity

import jakarta.persistence.*

@Entity(name = "Option")
data class OptionEntity(
        @Id
        @GeneratedValue
        @Column(name = "option_id")
        val id: Int,
        val name: String,
        val price: Int
)