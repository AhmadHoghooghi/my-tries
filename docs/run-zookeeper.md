[Apache Zookeeper on docker hub](https://hub.docker.com/_/zookeeper)

```shell
docker ps
```

```shell
docker stop zookeper393
```

```shell
docker rm zookeper393
```
```shell
export ZOOKEEPER_DOCKER_IMAGE=zookeeper:3.9.3
```

```shell
export ZOOKEEPER_DOCKER_IMAGE=docker.arvancloud.ir/zookeeper:3.9.3
```

Run a kafka in KRaft mode
```shell
$ docker run -d \
 --name zookeeper393 \
  --restart always \
   $ZOOKEEPER_DOCKER_IMAGE
```

_This image includes EXPOSE 2181 2888 3888 8080 
(the zookeeper client port, follower port, election port, AdminServer port respectively),
so standard container linking will make it automatically available to the linked containers.
Since the Zookeeper "fails fast" it's better to always restart it._