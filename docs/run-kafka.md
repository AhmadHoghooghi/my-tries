```shell
docker ps
```

```shell
docker stop kafka780
```

```shell
docker start kafka780
```

```shell
docker rm kafka780
```
```shell
export KAFKA_DOCKER_IMAGE=cp-kafka:7.8.0
```

```shell
export KAFKA_DOCKER_IMAGE=docker.arvancloud.ir/confluentinc/cp-kafka:7.8.0
```

Run a kafka in KRaft mode
```shell
docker run -d \
  --name kafka780 \
  --network host \
  -e KAFKA_NODE_ID=1 \
  -e KAFKA_PROCESS_ROLES=broker,controller \
  -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092,CONTROLLER://0.0.0.0:9093 \
  -e KAFKA_CONTROLLER_LISTENER_NAMES=CONTROLLER \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -e KAFKA_CONTROLLER_QUORUM_VOTERS=1@localhost:9093 \
  -e CLUSTER_ID="kafka-cluster-$(uuidgen)" \
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
  -e KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1 \
  $KAFKA_DOCKER_IMAGE
```