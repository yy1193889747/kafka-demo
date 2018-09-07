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

## 4. 不同群组消费同一个topic  `properties.put("auto.offset.reset", "earliest");`