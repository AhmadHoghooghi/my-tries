Implment with fixedBackOff

```java
BackOff backoff = new FixedBackOff(1000L, Long.MAX_VALUE);
CommonErrorHandler errorHandler = new DefaultErrorHandler(backoff);
factory.setCommonErrorHandler(errorHandler);
```

This is a good plan for ExponentialBackOff:

```java
BackOff backoff = new ExponentialBackOff(1000L, 1.1);
CommonErrorHandler errorHandler = new DefaultErrorHandler(backoff);
factory.setCommonErrorHandler(errorHandler);
```
