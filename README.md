# kafka study node
阅读书籍《Kafka权威指南》、《Apache Kafka源码剖析》
## 1. 监控平台 [KafkaOffsetMonitor](https://github.com/quantifind/KafkaOffsetMonitor) 搭建
```
nohup java -cp KafkaOffsetMonitor-assembly-0.2.1.jar \
     com.quantifind.kafka.offsetapp.OffsetGetterWeb \
     --zk localhost:2181 \
     --port 8080 \
     --refresh 10.seconds \
     --retain 2.days & nohup.out
```
## 2. 监控平台 [zkui](https://github.com/DeemOpen/zkui) 搭建
    nohup java -jar zkui-2.0-SNAPSHOT-jar-with-dependencies.jar &
注：(jar包和config.cfg在同级目录，修改相关配置。)

## 3. 监控平台 [kafka-web-console](https://github.com/claudemamo/kafka-web-console) 搭建
    nohup ./kafka-web-console -Dhttp.port=9001 >/dev/null 2>&1 &
注：([编译和打包教程](https://blog.csdn.net/hengyunabc/article/details/40431627))

## 4. 生产者
* 查看数据`bin/kafka-run-class.sh kafka.tools.DumpLogSegments --files /home/bill/tool/kafka/log/test-0/00000000000000000000.log`
* 向指定分区发数据`new ProducerRecord<>("strong-topic",1,System.currentTimeMillis(),"hello","world")`（或者实现自己的分区策略）

## 5. 消费者
* 不同群组消费同一个topic  `properties.put("auto.offset.reset", "earliest");`
* 从指定偏移量(分区)开始获取数据 `consumer.seek(new TopicPartition("test", 0), 370);`
    * subscribe订阅，则需先`consumer.poll(0)`，来自动分配分区
    * assign订阅，直接指定分区`consumer.assign(Collections.singleton(new TopicPartition("test", 0)))`，（使用assign,不会触发再均衡）
    
## 6. 主题
* 创建分区为8，副本为2的主题
`bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic strong-topic --replication-factor 2 --partitions 8 --disable-rack-aware`
* 查看主题
`bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic strong-topic`
