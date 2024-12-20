[Apache Zookeeper on docker hub](https://hub.docker.com/_/zookeeper)

```shell
docker ps
```

```shell
docker stop zookeeper393
```

```shell
docker rm zookeeper393
```
```shell
export ZOOKEEPER_DOCKER_IMAGE=zookeeper:3.9.3
```

```shell
export ZOOKEEPER_DOCKER_IMAGE=docker.arvancloud.ir/zookeeper:3.9.3
```

```shell
  docker run -d \
  --network host \
  --name zookeeper393 \
  --restart always \
   $ZOOKEEPER_DOCKER_IMAGE
```

_This image includes EXPOSE 2181 2888 3888 8080 
(the zookeeper client port, follower port, election port, AdminServer port respectively),
so standard container linking will make it automatically available to the linked containers.
Since the Zookeeper "fails fast" it's better to always restart it._

## Interact with ZooKeeper CLI

```shell
docker exec -it zookeeper393 bash
 
root@1aef6cfd660d:/apache-zookeeper-3.9.3-bin#
root@1aef6cfd660d:/apache-zookeeper-3.9.3-bin# cd bin/
root@1aef6cfd660d:/apache-zookeeper-3.9.3-bin/bin# ls
README.txt    zkCli.sh   zkServer.cmd            zkSnapshotComparer.cmd                 zkSnapshotRecursiveSummaryToolkit.sh  zkTxnLogToolkit.cmd
zkCleanup.sh  zkEnv.cmd  zkServer-initialize.sh  zkSnapshotComparer.sh                  zkSnapShotToolkit.cmd                 zkTxnLogToolkit.sh
zkCli.cmd     zkEnv.sh   zkServer.sh             zkSnapshotRecursiveSummaryToolkit.cmd  zkSnapShotToolkit.sh

root@1aef6cfd660d:/apache-zookeeper-3.9.3-bin/bin# ./zkCli.sh
Connecting to localhost:2181
JLine support is enabled
[zk: localhost:2181(CONNECTED) 0] help
[zk: localhost:2181(CONNECTED) 1] ls /
[zk: localhost:2181(CONNECTED) 2] create /zk_test my_data
[zk: localhost:2181(CONNECTED) 3] get /zk_test
[zk: localhost:2181(CONNECTED) 4] delete /zk_test
```