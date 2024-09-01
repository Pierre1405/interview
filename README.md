application to demonstrate technical skill
- kotlin
- docker
- Spring boot rest controller
- JPA
- liquibase
- get discount on max price algorithm
- unit test

- streaming, postgres -> kafka -> aggregation -> kafka -> mongodb with kafka connect and kafka stream ???
- sql
- nosql aggregation


# Startup

- Launch postgres
````bash
cd docker
docker-compose up -d
````
- Launch `vehicle-crud/src/main/kotlin/com/demo/interview/VehiculeCrudApplication.kt` application
- test with http://localhost:8080/vehicle/1 
- test with http://localhost:8080/vehicle/2
- test with http://localhost:8080/vehicle/1/discount/10
- test with http://localhost:8080/vehicle/2/discount/10

# Done
- kotlin
- docker
- Spring boot rest controller
- liquibase
- JPA
- get discount on max price algorithm

# Missing
- More validation
- Better exception management

