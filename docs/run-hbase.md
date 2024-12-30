hbase version:
2.1.10

https://hbase.apache.org/2.6/apidocs/org/apache/hadoop/hbase/client/package-summary.html

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

--network host \
-d
```shell
docker run -d  \
  --name=hbase212 \
  -h hbase-docker \
  -p 2181:2181 \
  -p 8080:8080 \
  -p 8085:8085 \
  -p 9090:9090 \
  -p 9095:9095 \
  -p 16010:16010 \
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
cat /etc/hosts
```

```shell
sudo vim /etc/hosts
```

```shell
docker restart hbase212 
```


https://www.baeldung.com/hbase