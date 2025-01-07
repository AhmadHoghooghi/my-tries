package com.rhotiz.logger.log;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PeriodicLogger {

    Logger logger = LoggerFactory.getLogger(PeriodicLogger.class);
    @Scheduled(fixedRate = 5000)
    public void periodicLog(){
        logger.info("Periodic log on {}", LocalDateTime.now());
    }
}
