Implment with fixedBackOff

```java
BackOff backoff = new FixedBackOff(1000L, Long.MAX_VALUE);
CommonErrorHandler errorHandler = new DefaultErrorHandler(backoff);
factory.setCommonErrorHandler(errorHandler);
```

