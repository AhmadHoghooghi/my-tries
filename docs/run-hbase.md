hbase version:
2.1.10


import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

https://hub.docker.com/r/dajobe/hbase/
https://github.com/dajobe/hbase-docker

```shell
docker ps
```

```shell
docker stop hbase212
```

```shell
docker start hbase212
```

```shell
docker rm hbase212
```

```shell
docker pull docker.arvancloud.ir/dajobe/hbase:latest
```
```shell
export HBASE_IMAGE=dajobe/hbase
```


```shell
export HBASE_IMAGE=docker.arvancloud.ir/dajobe/hbase
```

-d
```shell
docker run  \
  --name=hbase212 \
  -h hbase-docker \
  $HBASE_IMAGE
```

https://hbase.apache.org/book.html#configuration
```shell
docker exec -it hbase212 bash
root@hbase-docker:/# hbase shell
hbase(main):001:0> create 'test', 'cf'
hbase(main):002:0> list 'test'
hbase(main):003:0> describe 'test'
hbase(main):004:0> put 'test', 'row1', 'cf:a', 'value1'
hbase(main):007:0> get 'test', 'row1'
```


```shell
docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' hbase212
```

```shell
hbase212IP=$(docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' hbase212)
echo "$hbase212IP hbase-docker "
```
```shell
sudo vim /etc/hosts
```


https://www.baeldung.com/hbase