read more here: https://docs.spring.io/spring-kafka/reference/kafka/annotation-error-handling.html#backoff-handlers

Consider below construction of org.springframework.kafka.listener.`DefaultErrorHandler` 

`DefaultErrorHandler(ConsumerRecordRecoverer, BackOff, BackOffHandler)`

What is its role

The Hierarchy is:
- `org.springframework.kafka.listener.BackOffHandler`
- `org.springframework.kafka.listener.ContainerPausingBackOffHandler`
- `org.springframework.kafka.listener.DefaultBackOffHandler`

documentation says:
Handler for the provided back off time, listener container and exception. Also supports back off for individual partitions.

