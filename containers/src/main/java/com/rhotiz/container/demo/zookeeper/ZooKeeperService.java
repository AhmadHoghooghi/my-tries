package com.rhotiz.container.demo.zookeeper;

import jakarta.annotation.PostConstruct;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "zookeeper.interaction.action.enabled", havingValue = "true")
public class ZooKeeperService {

    private static final Logger log = LoggerFactory.getLogger(ZooKeeperService.class);
    @Autowired
    CuratorFramework zookeeperCuratorClient;
    private long i = 0;


    @PostConstruct
    public void createNodeIfNotExists() throws Exception {
        if (zookeeperCuratorClient.checkExists().forPath("/head") == null) {
            zookeeperCuratorClient.create()
                    .creatingParentContainersIfNeeded()
                    .forPath("/head", ZookeeperUtil.toByteArray("1"));
        }
    }


    @Scheduled(fixedRate = 10000L)
    private void updateValueInZookeeper() throws Exception {
        byte[] bytes = zookeeperCuratorClient.getData().forPath("/head");
        String currentValue = ZookeeperUtil.toString(bytes);
        log.info("Read from zookeeper: {}", currentValue);
        String newValue = currentValue + "," + i;
        zookeeperCuratorClient.setData().forPath("/head", ZookeeperUtil.toByteArray(newValue));
        i++;
    }




}


