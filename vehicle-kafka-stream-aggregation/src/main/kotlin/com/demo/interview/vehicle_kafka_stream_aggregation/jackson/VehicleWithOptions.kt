package com.demo.interview.vehicle_kafka_stream_aggregation.jackson

data class VehicleWithOptions(val vehicle_id: String? = null, val name: String? = null, val price: Int? = null, val option: List<Option> = emptyList())
