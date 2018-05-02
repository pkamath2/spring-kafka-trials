# Kafka Cheat Sheet

List of frequently used stuff - 

## Start/Stop
bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties

## Create & List
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic FX_RATES
bin/kafka-topics.sh --list --zookeeper localhost:2181
bin/kafka-topics.sh --topic FX_RATES --describe --zookeeper localhost:2181

## Delete Topic
bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic FX_RATES

##  Add partitions
bin/kafka-topics.sh --alter --zookeeper localhost:2181 --topic FX_RATES --partitions 2

## Command Line Consumer
Beware of --from-beginning!! 
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic FX_RATES --partition 1 --from-beginning

## Purging a topic
bin/kafka-configs.sh --zookeeper localhost:2181 --entity-name FX_RATES --entity-type topics --alter --add-config retention.ms=1000
then
bin/kafka-configs.sh --zookeeper localhost:2181 --entity-name FX_RATES --entity-type topics --alter --delete-config retention.ms
