package com.demo.interview.vehicle_kafka_stream_aggregation.processor

import com.demo.interview.vehicle_kafka_stream_aggregation.jackson.Option
import com.demo.interview.vehicle_kafka_stream_aggregation.jackson.Vehicle
import com.demo.interview.vehicle_kafka_stream_aggregation.jackson.VehicleWithOptions
import com.demo.interview.vehicle_kafka_stream_aggregation.serde.AppSerde.OPTION_SERDE
import com.demo.interview.vehicle_kafka_stream_aggregation.serde.AppSerde.STRING_SERDE
import com.demo.interview.vehicle_kafka_stream_aggregation.serde.AppSerde.VEHICLE_SERDE
import com.demo.interview.vehicle_kafka_stream_aggregation.serde.AppSerde.VEHICLE_WITH_OPTIONS_SERDE
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.*
import org.apache.kafka.streams.state.KeyValueStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Duration


@Component
class VehicleOptionCountProcessor {


    @Autowired
    fun buildPipeline(streamsBuilder: StreamsBuilder) {
        val vehicleStream = streamsBuilder
            .stream(INPUT_VEHICLE_TOPIC, Consumed.with(STRING_SERDE, VEHICLE_SERDE))
            .map { key, value -> KeyValue(value.vehicle_id, value) }
        val optionStream = streamsBuilder
            .stream(INPUT_OPTION_TOPIC, Consumed.with(STRING_SERDE, OPTION_SERDE))
            .map { key, value -> KeyValue(value.vehicle_id, value) }

        val vehicleOptionStream = vehicleStream
            .leftJoin(
                optionStream,
                { vehicle: Vehicle?, option: Option? -> Pair(vehicle, option) },
                JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(1)),
                StreamJoined.with(STRING_SERDE, VEHICLE_SERDE, OPTION_SERDE)
            )


        val groupByStream: KGroupedStream<String, Pair<Vehicle?, Option?>> = vehicleOptionStream
            .groupByKey(Grouped.`as`(""))

        val aggregateStream: KStream<String, VehicleWithOptions> = groupByStream
            .aggregate(
                { VehicleWithOptions() },
                { key, vehicleOptionPair, acc ->
                    val (vehicle, option) = vehicleOptionPair
                    acc.copy(
                        vehicle_id = vehicle?.vehicle_id,
                        name = vehicle?.name,
                        price = vehicle?.price,
                        option = if (option != null) acc.option.plus(option) else acc.option
                    )
                },
                Materialized
                    .`as`<String?, VehicleWithOptions?, KeyValueStore<Bytes, ByteArray>?>("vehicle_with_option_aggregation_state_store")
                    .withKeySerde(STRING_SERDE)
                    .withValueSerde(VEHICLE_WITH_OPTIONS_SERDE)
            )

            .toStream(Named.`as`("vehicle_with_option_aggregation_stream"))

    }

    companion object {
        const val OUTPUT_TOPIC = "vehicle_aggregate2"
        const val INPUT_VEHICLE_TOPIC = "postgres-01-vehicle"
        const val INPUT_OPTION_TOPIC = "postgres-01-option"
    }
}
