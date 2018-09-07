package com.cy.kafkademo.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author congyang.guo
 */

public class Consumer {
    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", "testssd");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("auto.offset.reset", "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        // 订阅所有与test相关的主题  consumer.subscribe("test.*") 自定分配分区
        consumer.subscribe(Collections.singletonList("test"));
        consumer.poll(0);
        // consumer.assign(Collections.singleton(new TopicPartition("test", 0)));
        // consumer.seek(new TopicPartition("test", 0), 370);
        // 获取topics
        System.out.println("------"+consumer.listTopics());

        HashMap<String, Integer> map = new HashMap<>(1 << 8);
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(record.key() + "---" + record.value() + "---" + record.offset());
                    int count = 1;
                    if (map.containsValue(record.value())) {
                        count = map.get(record.value()) + 1;
                    }
                    map.put(record.value(), count);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }

    }
}
