package com.demo.interview.vehicle_kafka_stream_aggregation.processor

import com.demo.interview.vehicle_kafka_stream_aggregation.jackson.Option
import com.demo.interview.vehicle_kafka_stream_aggregation.jackson.Vehicle
import com.demo.interview.vehicle_kafka_stream_aggregation.serde.JacksonSerde
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.serialization.Serdes.WrapperSerde
import org.apache.kafka.common.serialization.Serializer
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Duration


@Component
class VehicleOptionCountProcessor {

    private val objectMapper: ObjectMapper = jacksonObjectMapper()
    private val INTEGER_SERDE: Serde<Int> = Serdes.Integer()

    private val VEHICLE_SERDE: Serde<Vehicle> = JacksonSerde(Vehicle::class.java)
    private val OPTION_SERDE: Serde<Option> = JacksonSerde(Option::class.java)

    private val OPTION_VEHICLE_SERDE: Serde<Pair<Vehicle?, Option?>> = object : WrapperSerde<Pair<Vehicle?, Option?>>(
            Serializer { _, data -> objectMapper.writeValueAsBytes(data) },
            Deserializer { _, data -> objectMapper.readValue(String(data), objectMapper.typeFactory.constructParametricType(Pair::class.java, Vehicle::class.java, Option::class.java)) }
    ) {}


    @Autowired
    fun buildPipeline(streamsBuilder: StreamsBuilder) {
        val vehicleStream: KStream<Int, Vehicle> = streamsBuilder
                .stream(INPUT_VEHICLE_TOPIC, Consumed.with(INTEGER_SERDE, VEHICLE_SERDE))
                .map { key, value -> KeyValue(value.vehicle_id, value) }
        val optionStream: KStream<Int, Option> = streamsBuilder
                .stream(INPUT_OPTION_TOPIC, Consumed.with(INTEGER_SERDE, OPTION_SERDE))
                .map { key, value -> KeyValue(value.vehicle_id, value) }

        /*
        vehicleStream
                .to(OUTPUT_TOPIC, Produced.with(INTEGER_SERDE, VEHICLE_SERDE))
        optionStream
                .to(OUTPUT_TOPIC, Produced.with(INTEGER_SERDE, OPTION_SERDE))
        */
        vehicleStream.outerJoin(
                optionStream,
                { vehicle, option -> Pair(vehicle, option) },
                JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(1)),
                StreamJoined.with(INTEGER_SERDE, VEHICLE_SERDE, OPTION_SERDE)
        )
                .to(OUTPUT_TOPIC, Produced.with(INTEGER_SERDE, OPTION_VEHICLE_SERDE))
    }

    companion object {
        const val OUTPUT_TOPIC = "vehicle_aggregate"
        const val INPUT_VEHICLE_TOPIC = "postgres-01-vehicle"
        const val INPUT_OPTION_TOPIC = "postgres-01-option"
    }
}
