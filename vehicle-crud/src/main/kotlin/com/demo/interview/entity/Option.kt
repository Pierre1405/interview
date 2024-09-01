package com.demo.interview.entity

import jakarta.persistence.*

@Entity
data class Option(
        @Id
        @GeneratedValue
        @Column(name = "option_id")
        val id: Int,
        val name: String,
        val price: Int
)