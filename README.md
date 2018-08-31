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
