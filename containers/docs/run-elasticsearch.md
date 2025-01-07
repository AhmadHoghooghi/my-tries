elasticsearch:8.13.4

```shell
export ELASTIC_IMAGE=docker.arvancloud.ir/elasticsearch:8.13.4
```
```shell
docker pull $ELASTIC_IMAGE
```

```shell
docker run -d \
  --name=elasticsearch8134 \
  -p 9200:9200 \
  -p 9300:9300 \
  -e "discovery.type=single-node" \
  -e "xpack.security.enabled=false" \
  $ELASTIC_IMAGE
```

```shell
curl -X GET "http://localhost:9200/"
```

```shell
curl -X PUT "http://localhost:9200/hello-world"
```

```shell
curl -X POST "http://localhost:9200/hello-world/_doc/1" -H 'Content-Type: application/json' -d'
{
  "message": "Hello, World!"
}'
```

```shell
curl -X GET "http://localhost:9200/_cat/indices?v"
```
```shell
curl -X GET "http://localhost:9200/hello-world/_search"
```

```shell
curl -X GET "http://localhost:9200/log4j2/_search" | jq
```

```shell
curl -X GET "http://localhost:9200/log4j2/_search" -H 'Content-Type: application/json' -d'
{
  "size": 1,
  "sort": [
    {
      "timeMillis": {
        "order": "desc"
      }
    }
  ]
}' | jq
```


```shell
curl -X GET "http://localhost:9200/log4j2/_search" -H 'Content-Type: application/json' -d'
{
  "size": 1,
  "sort": [
    {
      "@timestamp": {
        "order": "desc"
      }
    }
  ]
}' | jq
```


```shell
curl  -X DELETE "http://localhost:9200/log4j2"
```

```shell
curl -X GET "http://localhost:9200/log4j2/_search" | jq
```
