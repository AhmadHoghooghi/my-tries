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

# next message:
What is the reaction of spring kafka when sending two messages, one having error and 2nd without error?
is the having error message acked and next message is consumed?
Consider a code like this
```java
@Scheduled(fixedRate = 5*60*1000L)
public void sendCommands() {
    String executionId = ExecutionUtil.getExecutionId();
    MyCommand myCommand1 = new MyCommand(executionId, commandId++, 1);
    System.out.println("Sending "+ myCommand1);
    kafkaTemplate.send(Constants.TOPIC, myCommand1);
    MyCommand myCommand2 = new MyCommand(executionId, commandId++, 0);
    System.out.println("Sending "+ myCommand2);
    kafkaTemplate.send(Constants.TOPIC, myCommand2);
}
```

```text
commonErrorHandler.isAckAfterHandle(): false
Sending I-1-Err1
Sending I-2-Err0

Received message: F-1-Err1
[Tries to Recieve F-1-Err1]
[Stack Trace for F-1-Err1]

Received message: G-1-Err1
[Tries to Recieve G-1-Err1]
[Stack Trace for G-1-Err1]

Received message: H-1-Err1
[Tries to Recieve H-1-Err1]
[Stack Trace for H-1-Err1]

Received message: I-1-Err1
[Tries to Recieve I-1-Err1]
[Stack Trace for I-1-Err1]

Received message: I-2-Err0
```
and if run the app again

```text
commonErrorHandler.isAckAfterHandle(): false
Sending J-1-Err1
Sending J-2-Err0

Received message: J-1-Err1
[Tries to Recieve J-1-Err1]
[Stack Trace for J-1-Err1]
Received message: J-2-Err0
```
Even though base on `commonErrorHandler.isAckAfterHandle(): false` in previous execution there wasn't ack for 
`I-1-Err1` but `I-2-Err0` acked and in the other execution (J), I was not consumed again.