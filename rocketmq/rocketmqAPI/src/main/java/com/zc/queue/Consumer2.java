package com.zc.queue;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: zhouchao
 * @Date: 2018/10/11 10:09
 * @Description: *****************************************************************************************
 * 先启动消费端，再启动服务提供端（先订阅再注册）
 * *****************************************************************************************
 */
public class Consumer2 {
    public Consumer2() throws MQClientException {
        String group_name = "order_consumer";
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(group_name);
        consumer.setNamesrvAddr("192.168.94.11:9876;192.168.94.12:9876;192.168.94.13:9876;192.168.94.14:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("TopicTest", "*");
        consumer.registerMessageListener(new Listener());
        consumer.start();
        System.out.println("consumer2 started");
    }

    class Listener implements MessageListenerOrderly {
        private Random random = new Random();
        @Override
        public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
            consumeOrderlyContext.setAutoCommit(true);
            for (MessageExt msg : list) {
                System.out.println(msg+" , content:"+new String(msg.getBody()));
            }
            try {
                TimeUnit.SECONDS.sleep(random.nextInt(5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return ConsumeOrderlyStatus.SUCCESS;
        }
    }

    public static void main(String[] args) throws Exception {
        new Consumer2();
    }
}
