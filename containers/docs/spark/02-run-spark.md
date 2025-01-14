3.5.4-scala2.12-java17-ubuntu
https://hub.docker.com/r/bitnami/spark
https://hub.docker.com/_/spark

```shell
docker ps
```
```shell
docker compose up
```

```shell
mkdir -p /tmp/spark-cluster/spark-events
sudo chown -R 1001:1001 /tmp/spark-cluster/spark-events
```

```shell
sudo rm -rf /tmp/spark-cluster/spark-events/*
```

```shell
docker compose up -d
```

```shell
docker compose down
```

```shell
docker compose restart
```

```shell
sudo rm -rf /tmp/spark-cluster/
```

```shell
cd /opt/bitnami/spark/bin
```
```shell
docker exec -it spark-master bash
spark-shell
spark.range(1000 * 1000 * 1000).count()
```