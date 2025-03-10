DefaultErrorHandler has a field with default value equal to `true`
`org.springframework.kafka.listener.DefaultErrorHandler.ackAfterHandle`

This means after error handling is done there is an ack.

The result of setting field to false is tested and its like this:


```text
commonErrorHandler.isAckAfterHandle(): false
Sending H-1-Err1
Received message: F-1-Err1
[Tries to Recieve F-1-Err1]
[Stack Trace for F-1-Err1]

Received message: G-1-Err1
[Tries to Recieve G-1-Err1]
[Stack Trace for G-1-Err1]

Received message: H-1-Err1
[Tries to Recieve H-1-Err1]
[Stack Trace for H-1-Err1]
```

F and G are messages sent to kafka from previous executions and there was no ack for them.