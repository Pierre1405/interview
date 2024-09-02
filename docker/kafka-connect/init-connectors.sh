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
    "transforms.createKey.fields":"option_id",
    "transforms.extractInt.type":"org.apache.kafka.connect.transforms.ExtractField$Key",
    "transforms.extractInt.field":"option_id"
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
    "transforms.extractInt.field":"vehicle_id"
  }
}
'