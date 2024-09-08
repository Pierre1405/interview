# Purpose of this application

After 8 months without coding (except training), it was very hard for me to remember
the syntax, I really had the feeling that I could have done better for 
the technical test, so I decide to create this application in order to demonstrate 
what I can do in normal working condition.

### Demonstrated skills

The current version of the application should demonstrate the following technical skills
- kotlin
- Spring boot
- JPA
- liquibase
- get discount on max price algorithm
- unit test
- basic service, dao, controller, etc... architecture
- docker
- streaming, postgres -> kafka -> aggregation -> kafka with kafka connect and kafka stream

# Scenario
- It's a simple read only catalog application. It displays cars with a list of options
- At startup, the application will initialize the DB with 2 tables and fill them with 2 cars and their options
```
Vehicle(vehicule_id, name, price)
Vehicle(option_id, vehicule_id, name, price)
```
you can check the `vehicle-crud/src/main/resources/db/changelog/db.changelog-master.xml` file for more details
- There is 2 endpoints

  `http://localhost:8080/vehicle/{id}`
  returns the car with the vehicule_id == {id} and the list of the options

  `http://localhost:8080/vehicle/{id}/discount/{discount}`
  returns the car with the vehicule_id == {id} and the list of the options, it
  also apply a percent discount ({discount}) on the most expensive option round to the lowest integer

# Run application

### Startup

- Launch postgres
````bash
cd docker
docker-compose up -d
````
- Launch `vehicle-crud/src/main/kotlin/com/demo/interview/VehiculeCrudApplication.kt` application
- Wait for a few minutes and launch `docker/kafka-connect/init-connectors.sh` application. You should get a 200 status if kafka connect had enough time to start.
- Launch `vehicle-kafka-stream-aggregation/src/main/kotlin/com/demo/interview/vehicle_kafka_stream_aggregation/VehicleKafkaStreamAggregationApplication.kt` application



### Test the application

- test with http://localhost:8080/vehicle/1 , you should have the following result
```json
{
  id: 1,
  name: "Twingo",
  price: 123,
  optionDto: [
    {
      id: 1,
      name: "pink painting",
      price: 34
    },
    {
      id: 2,
      name: "cd player",
      price: 12
    }
  ]
}
```
- test with http://localhost:8080/vehicle/1/discount/10 , the option pink painting should be cheaper
```json
{
  id: 1,
  name: "Twingo",
  price: 123,
  optionDto: [
    {
      id: 1,
      name: "pink painting",
      price: 30
    },
    {
      id: 2,
      name: "cd player",
      price: 12
    }
  ]
}
```
- you can also test with the car with id 2
- You can also try the `VehicleDtoEntityControllerTest` and `VehicleServiceImplTest` tests
- Check the 2 following urls to check if kafka connect is streaming options and vehicle from postgres to kafka
  `http://localhost:8081/ui/clusters/local/all-topics/postgres-01-option/messages?keySerde=String&valueSerde=SchemaRegistry&limit=100`
  `http://localhost:8081/ui/clusters/local/all-topics/postgres-01-vehicle/messages?keySerde=String&valueSerde=SchemaRegistry&limit=100`
- Check the following url to check if kafka stream did the joint between options and vehicle from kafka to kafka
  `http://localhost:8081/ui/clusters/local/all-topics/vehicle-kafka-stream-aggregation-vehicle_with_option_aggregation_state_store-changelog/messages?keySerde=String&valueSerde=SchemaRegistry&limit=100`


# Work in progress

### Missing feature
- More validation
- Better exception management
- No getAll, POST, PUT, DELETE api
- Swagger documentation
- no avro serialization on kafka

### Missing demonstration
I didn't have the time to do so much, but, I would have liked to do more. Like create
another microservice to stream aggregated data on a no sql db
- kafka -> mongodb with kafka connect ???
- basic sql jdbc queries
- nosql aggregation

