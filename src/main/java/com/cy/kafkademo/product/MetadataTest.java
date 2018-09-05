package com.cy.kafkademo.product;

import org.apache.kafka.common.Cluster;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author congyang.guo
 * @Title: MetadataTest
 * @ProjectName kafka-demo
 * @Description: TODO
 * @date 2018/9/4下午7:06
 */
public class MetadataTest {
    public static void main(String[] args) {
        // 初始化metedata 默认值
        List<InetSocketAddress> addresses = new ArrayList<>();
        addresses.add(new InetSocketAddress("loclhost", 9092));
        System.out.println(Cluster.bootstrap(addresses).topics());
    }
}
