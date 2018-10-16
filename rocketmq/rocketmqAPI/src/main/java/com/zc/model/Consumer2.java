package com.zc.model;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @Auther: zhouchao
 * @Date: 2018/10/12 21:56
 * @Description:
 * *****************************************************
 * consumerGroup一样时，可以实现消费者之间消息消费的的负载均衡
 * *****************************************************
 */
public class Consumer2 {
    public Consumer2() {
        try {
            String consumerGroup = "message_consumer";
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
            consumer.setNamesrvAddr("192.168.94.11:9876;192.168.94.12:9876;192.168.94.13:9876;192.168.94.14:9876");
            consumer.subscribe("Topic1", "Tag1");
            consumer.registerMessageListener(new Listener2());
            consumer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Listener2 implements MessageListenerConcurrently {
        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
            try {
                for (MessageExt msg : msgs) {
                    String topic = msg.getTopic();
                    String tags = msg.getTags();
                    String msgBody = new String(msg.getBody(), "utf-8");
                    System.out.println("收到消息：Topic："+topic+"tags："+tags+"Message："+msgBody);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
    }

    public static void main(String[] args) {
        new Consumer2();
        System.out.println("consumer2 started");
    }
}
