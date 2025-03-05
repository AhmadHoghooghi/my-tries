```shell
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  --data '{
    "schema": "{\"namespace\":\"com.rhotiz.avro\",\"type\":\"record\",\"name\":\"MyMessage\",\"fields\":[{\"name\":\"message\",\"type\":\"string\"}]}"
  }' \
  http://localhost:8081/subjects/my-message-topic-value/versions
```


```shell
curl -X GET http://localhost:8081/subjects/my-message-topic-value/versions
```


```shell
curl -X GET http://localhost:8081/subjects/my-message-topic-value/versions/1
```