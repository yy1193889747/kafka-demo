package com.cy.kafkademo.consumer;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

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
        properties.put("group.id", "testgroup");
        properties.put("auto.commit.interval.ms", "1000");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        // 订阅所有与test相关的主题  consumer.subscribe("test.*")
        consumer.subscribe(Collections.singletonList("test"));
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

                    System.out.println(JSONObject.toJSONString(map));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }

    }
}
