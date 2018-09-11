package com.cy.kafkademo.product;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * @author congyang.guo
 * @Title: MyPartition
 * @ProjectName kafka-demo
 * @Description: TODO
 * @date 2018/9/11上午9:40
 */
public class MyPartition implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        if ("hello".equals(key)) {
            return 1;
        }
        if ("hi".equals(key)) {
            return 2;
        }
        if ("nihao".equals(key)) {
            return 3;
        }
        if ("goodbye".equals(key)) {
            return 4;
        }
        return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
