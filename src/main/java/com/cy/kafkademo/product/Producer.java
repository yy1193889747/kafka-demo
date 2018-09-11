package com.cy.kafkademo.product;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @author congyang.guo
 **/
public class Producer {

    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "bill:9092,bill:9093,bill:9094");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // 指定压缩算法 none gzip snappy lz4
        properties.put("compression.type", "snappy");
        // 指定自定义分区策略
        properties.put("partitioner.class", "com.cy.kafkademo.product.MyPartition");


        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        // 指定分区
        // ProducerRecord<String, String> record = new ProducerRecord<>("strong-topic",1,System.currentTimeMillis(),"hello","world");
        int i = 1;
        try {
            while (true) {
                ProducerRecord<String, String> record = new ProducerRecord<>("strong-topic","hello", ""+i++);
                producer.send(record);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}





