package com.demo.interview.vehicle_kafka_stream_aggregation.serde

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serdes.WrapperSerde
import org.apache.kafka.common.serialization.Serializer


class JacksonSerde<T>(type: Class<T>) : WrapperSerde<T>(
        Serializer { _, data: T -> jacksonObjectMapper.writeValueAsBytes(data) },
        Deserializer { _, data -> jacksonObjectMapper.readValue(String(data), type) }
) {
    companion object {
        private val jacksonObjectMapper = jacksonObjectMapper()
    }
}