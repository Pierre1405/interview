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
        val optionStream = streamsBuilder
            .stream(INPUT_OPTION_TOPIC, Consumed.with(STRING_SERDE, OPTION_SERDE))

        val vehicleOptionStream = vehicleStream
            .leftJoin(
                optionStream,
                { vehicle: Vehicle?, option: Option? -> Pair(vehicle, option) },
                JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(1)),
                StreamJoined.with(STRING_SERDE, VEHICLE_SERDE, OPTION_SERDE).withStoreName("vehicle_join_with_option")
            )

        val groupByStream: KGroupedStream<String, Pair<Vehicle?, Option?>> = vehicleOptionStream
            .groupByKey(Grouped.`as`("vehicle_option_group_by_key"))

        groupByStream
            .aggregate(
                { VehicleWithOptions() },
                { key, vehicleOptionPair, acc ->
                    val (vehicle, option) = vehicleOptionPair
                    acc.copy(
                        _id = vehicle?.vehicle_id,
                        vehicle_id = vehicle?.vehicle_id,
                        name = vehicle?.name,
                        price = vehicle?.price,
                        option = if (option != null) acc.option.plus(option) else acc.option
                    )
                },
                Materialized
                    .`as`<String?, VehicleWithOptions?, KeyValueStore<Bytes, ByteArray>?>("vehicle_with_option_aggregation")
                    .withKeySerde(STRING_SERDE)
                    .withValueSerde(VEHICLE_WITH_OPTIONS_SERDE)
            )
            .toStream()
            .to(OUTPUT_TOPIC)

    }

    companion object {
        const val INPUT_VEHICLE_TOPIC = "postgres-01-vehicle"
        const val INPUT_OPTION_TOPIC = "postgres-01-option"
        const val OUTPUT_TOPIC = "aggregate-vehicle"
    }
}
