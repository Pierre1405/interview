curl --location 'http://localhost:8083/connectors' \
--header 'Accept-Language: fr' \
--header 'Content-Type: application/json' \
--data '{
  "name": "jdbc_source_postgres_option",
  "config": {
    "connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector",
    "connection.url": "jdbc:postgresql://postgres:5432/interview",
    "connection.user": "interview",
    "connection.password": "S3cret",
    "topic.prefix": "postgres-01-",
    "poll.interval.ms" : 3600000,
    "table.whitelist" : "option",
    "mode":"bulk",
    "transforms":"createKey,extractInt",
    "transforms.createKey.type":"org.apache.kafka.connect.transforms.ValueToKey",
    "transforms.createKey.fields":"vehicle_id",
    "transforms.extractInt.type":"org.apache.kafka.connect.transforms.ExtractField$Key",
    "transforms.extractInt.field":"vehicle_id",
    "value.converter":"org.apache.kafka.connect.json.JsonConverter",
    "value.converter.schemas.enable":"false"
  }
}
'

curl --location 'http://localhost:8083/connectors' \
--header 'Accept-Language: fr' \
--header 'Content-Type: application/json' \
--data '{
  "name": "jdbc_source_postgres_vehicle",
  "config": {
    "connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector",
    "connection.url": "jdbc:postgresql://postgres:5432/interview",
    "connection.user": "interview",
    "connection.password": "S3cret",
    "topic.prefix": "postgres-01-",
    "poll.interval.ms" : 3600000,
    "table.whitelist" : "vehicle",
    "mode":"bulk",
    "transforms":"createKey,extractInt",
    "transforms.createKey.type":"org.apache.kafka.connect.transforms.ValueToKey",
    "transforms.createKey.fields":"vehicle_id",
    "transforms.extractInt.type":"org.apache.kafka.connect.transforms.ExtractField$Key",
    "transforms.extractInt.field":"vehicle_id",
    "value.converter":"org.apache.kafka.connect.json.JsonConverter",
    "value.converter.schemas.enable":"false"
  }
}
'

#curl --location 'http://localhost:8083/connectors' \
#--header 'Accept-Language: fr' \
#--header 'Content-Type: application/json' \
#--data '{
#          "name": "sink_mongodb_vehicle",
#          "config": {
#            "topics": "aggregate-vehicle",
#            "connector.class": "com.mongodb.kafka.connect.MongoSinkConnector",
#            "connection.uri": "mongodb://mongodb:27017",
#            "key.converter": "org.apache.kafka.connect.storage.StringConverter",
#            "value.converter": "org.apache.kafka.connect.json.JsonConverter",
#            "value.converter.schemas.enable": false,
#            "database": "interview",
#            "collection": "vehicle"
#          }
#        }
#'


curl --location 'http://localhost:8083/connectors' \
--header 'Accept-Language: fr' \
--header 'Content-Type: application/json' \
--data '{
          "name": "sink_s3_parquet_vehicle",
          "config": {
            "topics": "aggregate-vehicle",
            "connector.class": "io.confluent.connect.s3.S3SinkConnector",
            "format.class": "io.confluent.connect.s3.format.json.JsonFormat",
            "s3.bucket.name": "interview",
            "store.url": "http://localstack:4566",
            "flush.size": "3",
            "storage.class": "io.confluent.connect.s3.storage.S3Storage",
            "format.class": "io.confluent.connect.s3.format.json.JsonFormat",
            "s3.compression.type": "none",
            "key.converter": "org.apache.kafka.connect.storage.StringConverter",
            "value.converter": "org.apache.kafka.connect.json.JsonConverter",
            "value.converter.schemas.enable":"false",
            "aws.access.key.id": "test",
            "aws.secret.access.key": "test"
          }
        }
'