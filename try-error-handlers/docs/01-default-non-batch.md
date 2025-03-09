in default mode
### **Default Behavior for Non-Batch Listeners**
1. **Error Handling**:
    - If an exception is thrown by the listener, the **`SeekToCurrentErrorHandler`** is used by default.
    - This handler **seeks back to the current offset** of the failed record, causing it to be reprocessed in the next poll.
    - By default, it retries **indefinitely** (no backoff).

2. **Acknowledgment**:
    - If `enable.auto.commit` is `false` (default in Spring Kafka), offsets are committed **only after successful processing**.
    - If `enable.auto.commit` is `true`, offsets are committed automatically, regardless of processing success.

3. **Logging**:
    - Errors are logged at the `ERROR` level by default.

---

the method in invoked 10 times.
see the log below:

```text
23:27:58.550+03:30 o.a.kafka.clients.consumer.internals.ConsumerUtils : Setting offset for partition error-trying-topic-0 to the committed offset FetchPosition{offset=62, offsetEpoch=Optional.empty, currentLeader=LeaderAndEpoch{leader=Optional[localhost:9092 (id: 1 rack: null)], epoch=2}}
23:27:58.554+03:30 o.s.kafka.listener.KafkaMessageListenerContainer : my-group-id: partitions assigned: [error-trying-topic-0]
Received message: K-1-Err1
23:27:58.621+03:30 o.a.k.c.consumer.internals.LegacyKafkaConsumer : [Consumer clientId=consumer-my-group-id-1, groupId=my-group-id] Seeking to offset 62 for partition error-trying-topic-0
23:27:58.621+03:30 o.s.kafka.listener.KafkaMessageListenerContainer : Record in retry and not yet recovered
Received message: K-1-Err1
23:27:59.115+03:30 o.a.k.c.consumer.internals.LegacyKafkaConsumer : [Consumer clientId=consumer-my-group-id-1, groupId=my-group-id] Seeking to offset 62 for partition error-trying-topic-0
23:27:59.115+03:30 o.s.kafka.listener.KafkaMessageListenerContainer : Record in retry and not yet recovered
Received message: K-1-Err1
23:27:59.621+03:30 o.a.k.c.consumer.internals.LegacyKafkaConsumer : [Consumer clientId=consumer-my-group-id-1, groupId=my-group-id] Seeking to offset 62 for partition error-trying-topic-0
23:27:59.621+03:30 o.s.kafka.listener.KafkaMessageListenerContainer : Record in retry and not yet recovered
Received message: K-1-Err1
23:28:00.131+03:30 o.a.k.c.consumer.internals.LegacyKafkaConsumer : [Consumer clientId=consumer-my-group-id-1, groupId=my-group-id] Seeking to offset 62 for partition error-trying-topic-0
23:28:00.131+03:30 o.s.kafka.listener.KafkaMessageListenerContainer : Record in retry and not yet recovered
Received message: K-1-Err1
23:28:00.643+03:30 o.a.k.c.consumer.internals.LegacyKafkaConsumer : [Consumer clientId=consumer-my-group-id-1, groupId=my-group-id] Seeking to offset 62 for partition error-trying-topic-0
23:28:00.644+03:30 o.s.kafka.listener.KafkaMessageListenerContainer : Record in retry and not yet recovered
Received message: K-1-Err1
23:28:01.146+03:30 o.a.k.c.consumer.internals.LegacyKafkaConsumer : [Consumer clientId=consumer-my-group-id-1, groupId=my-group-id] Seeking to offset 62 for partition error-trying-topic-0
23:28:01.146+03:30 o.s.kafka.listener.KafkaMessageListenerContainer : Record in retry and not yet recovered
Received message: K-1-Err1
23:28:01.657+03:30 o.a.k.c.consumer.internals.LegacyKafkaConsumer : [Consumer clientId=consumer-my-group-id-1, groupId=my-group-id] Seeking to offset 62 for partition error-trying-topic-0
23:28:01.658+03:30 o.s.kafka.listener.KafkaMessageListenerContainer : Record in retry and not yet recovered
Received message: K-1-Err1
23:28:02.164+03:30 o.a.k.c.consumer.internals.LegacyKafkaConsumer : [Consumer clientId=consumer-my-group-id-1, groupId=my-group-id] Seeking to offset 62 for partition error-trying-topic-0
23:28:02.164+03:30 o.s.kafka.listener.KafkaMessageListenerContainer : Record in retry and not yet recovered
Received message: K-1-Err1
23:28:02.672+03:30 o.a.k.c.consumer.internals.LegacyKafkaConsumer : [Consumer clientId=consumer-my-group-id-1, groupId=my-group-id] Seeking to offset 62 for partition error-trying-topic-0
23:28:02.672+03:30 o.s.kafka.listener.KafkaMessageListenerContainer : Record in retry and not yet recovered
Received message: K-1-Err1
23:28:03.184+03:30 o.s.kafka.listener.DefaultErrorHandler : Backoff FixedBackOff{interval=0, currentAttempts=10, maxAttempts=9} exhausted for error-trying-topic-0@62
```
we see Received message for 10 times, according to log its 10th attemt and maxAttemsts are 9.
It works by Fixed back off and based on logs it is retryie in ~ 500 ms
