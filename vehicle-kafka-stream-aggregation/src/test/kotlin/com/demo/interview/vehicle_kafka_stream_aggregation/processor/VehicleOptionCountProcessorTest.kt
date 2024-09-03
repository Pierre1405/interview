import com.demo.interview.vehicle_kafka_stream_aggregation.jackson.Vehicle
import com.demo.interview.vehicle_kafka_stream_aggregation.processor.VehicleOptionCountProcessor
import com.demo.interview.vehicle_kafka_stream_aggregation.processor.VehicleOptionCountProcessor.Companion.INPUT_OPTION_TOPIC
import com.demo.interview.vehicle_kafka_stream_aggregation.processor.VehicleOptionCountProcessor.Companion.INPUT_VEHICLE_TOPIC
import com.demo.interview.vehicle_kafka_stream_aggregation.processor.VehicleOptionCountProcessor.Companion.OUTPUT_TOPIC
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.commons.logging.LogFactory
import org.apache.kafka.common.serialization.*
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG
import org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG
import org.apache.kafka.streams.TopologyTestDriver
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*


@SpringBootTest(classes = arrayOf(com.demo.interview.vehicle_kafka_stream_aggregation.VehicleKafkaStreamAggregationApplication::class))
class VehicleOptionCountProcessorTest {

    private val log = LogFactory.getLog(VehicleOptionCountProcessor::class.java)

    @Autowired
    private lateinit var vehicleOptionCountProcessor: VehicleOptionCountProcessor

    private lateinit var streamsBuilder: StreamsBuilder

    @BeforeEach
    fun setUp() {
        streamsBuilder = StreamsBuilder()
    }

    @Test
    fun basic_test() {
        val vehicleJson1 = """{"vehicle_id":1,"name":"Twingo","price":10000}"""
        val vehicleJson2 = """{"vehicle_id":2,"name":"Clio","price":10000}"""
        val vehicleJson3 = """{"vehicle_id":3,"name":"No option","price":10000}"""
        val optionJson1 = """{"option_id":1,"vehicle_id":1,"name":"Sunroof","price":500}"""
        val optionJson2 = """{"option_id":2,"vehicle_id":2,"name":"Sunroof","price":500}"""
        val optionJson3 = """{"option_id":3,"vehicle_id":2,"name":"CD player","price":500}"""
        val optionJson4 = """{"option_id":4,"vehicle_id":2,"name":"Pink paint","price":500}"""

        vehicleOptionCountProcessor.buildPipeline(streamsBuilder)
        val topology = streamsBuilder.build()

        val properties = Properties()
        properties.setProperty(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().javaClass.name)
        properties.setProperty(DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().javaClass.name)
        val topologyTestDriver = TopologyTestDriver(topology, properties)

        val vehicleInputTopic = topologyTestDriver.createInputTopic(INPUT_VEHICLE_TOPIC, IntegerSerializer(), StringSerializer())
        val optionInputTopic = topologyTestDriver.createInputTopic(INPUT_OPTION_TOPIC, IntegerSerializer(), StringSerializer())
        val outputTopic = topologyTestDriver.createOutputTopic(OUTPUT_TOPIC, IntegerDeserializer(), StringDeserializer())

        vehicleInputTopic.pipeInput(1, vehicleJson1)
        vehicleInputTopic.pipeInput(2, vehicleJson2)
        vehicleInputTopic.pipeInput(3, vehicleJson3)
        optionInputTopic.pipeInput(1, optionJson1)
        optionInputTopic.pipeInput(2, optionJson2)
        optionInputTopic.pipeInput(3, optionJson3)
        optionInputTopic.pipeInput(4, optionJson4)

        val readKeyValuesToList = outputTopic.readKeyValuesToList()
        log.info(readKeyValuesToList)
    }
}

