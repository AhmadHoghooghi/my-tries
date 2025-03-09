# DefaultErrorHandler
study code of Default Error Handler, its implementation and it customization points.

It seems we can use it like this: `factory.setCommonErrorHandler(errorHandler);`

# Decorator pattern like Error Handlers:

read and understand this code by documentation:
```java
// Unlimited retries with backoff (e.g., 1s, 2s, 4s, 8s...)
ExponentialBackOffWithMaxRetries backOff = new ExponentialBackOffWithMaxRetries();
backOff.setInitialInterval(1000L); // Initial delay: 1 second
backOff.setMultiplier(2.0); // Exponential multiplier
backOff.setMaxInterval(60000L); // Max delay: 60 seconds
backOff.setMaxAttempts(Integer.MAX_VALUE); // "Unlimited" retries

// Configure error handler
DefaultErrorHandler errorHandler = new DefaultErrorHandler(
    new DeadLetterPublishingRecoverer(kafkaTemplate), // Optional DLT fallback
    backOff
);

factory.setCommonErrorHandler(errorHandler);
```

# next message:
What is the reaction of spring kafka for next message?
is the having error message acked and next message is consumed?

# stop spring boot app
how can we stop a spring boot app in case we want to stop app after retries

# Error Handlers Hierarchy
give me a hierarchy of error handlers with description
`org.springframework.kafka.listener.DefaultErrorHandler`
classes like: 
- `org.springframework.kafka.listener.FailedBatchProcessor`
- `org.springframework.kafka.listener.FailedRecordProcessor`

# Read error handling from documentation

# ask Deepseek to describe ErrorHandlers
ignore error handlers related to DLT and transaction management

# what is concept of exhust
test with code

# what is concept of Recover
test with code

# Study about back off:
Hierarchy: 
- `org.springframework.util.backoff.BackOff`
- `org.springframework.util.backoff.FixedBackOff`
- `org.springframework.util.backoff.ExponentialBackOff`


# Hierarchy of Recoverers:
Default Recoverer: Construct an instance with the default recoverer. (see documentations for constructor of)
`org.springframework.kafka.listener.DefaultErrorHandler.DefaultErrorHandler(org.springframework.util.backoff.BackOff)`
- `org.springframework.kafka.listener.ConsumerRecordRecoverer`
- `org.springframework.kafka.listener.ConsumerAwareRecordRecoverer`
- `org.springframework.kafka.listener.DeadLetterPublishingRecoverer`

# test `org.springframework.kafka.listener.DefaultErrorHandler.setCommitRecovered`

# What is concept of back off handler in
`DefaultErrorHandler.DefaultErrorHandler(ConsumerRecordRecoverer, BackOff, BackOffHandler)`
