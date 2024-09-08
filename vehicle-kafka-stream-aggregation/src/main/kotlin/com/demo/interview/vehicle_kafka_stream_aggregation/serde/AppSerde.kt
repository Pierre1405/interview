package com.demo.interview.vehicle_kafka_stream_aggregation.serde

import com.demo.interview.vehicle_kafka_stream_aggregation.jackson.Option
import com.demo.interview.vehicle_kafka_stream_aggregation.jackson.Vehicle
import com.demo.interview.vehicle_kafka_stream_aggregation.jackson.VehicleWithOptions
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.serialization.Serializer

object AppSerde {
    private var objectMapper: ObjectMapper = jacksonObjectMapper()

    val INTEGER_SERDE: Serde<Int> = Serdes.Integer()
    val STRING_SERDE: Serde<String> = Serdes.String()
    val VEHICLE_SERDE: Serde<Vehicle> = JacksonSerde(Vehicle::class.java)
    val OPTION_SERDE: Serde<Option> = JacksonSerde(Option::class.java)
    val OPTION_VEHICLE_SERDE: Serde<Pair<Vehicle?, Option?>> = object : Serdes.WrapperSerde<Pair<Vehicle?, Option?>>(
            Serializer { _, data -> objectMapper.writeValueAsBytes(data) },
            Deserializer { _, data -> objectMapper.readValue(data, objectMapper.typeFactory.constructParametricType(Pair::class.java, Vehicle::class.java, Option::class.java)) }
    ) {}
    val VEHICLE_WITH_OPTIONS_SERDE: Serde<VehicleWithOptions> = JacksonSerde(VehicleWithOptions::class.java)
}