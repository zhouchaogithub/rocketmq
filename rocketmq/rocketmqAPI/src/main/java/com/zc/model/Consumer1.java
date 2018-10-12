package com.zc.model;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @Auther: zhouchao
 * @Date: 2018/10/12 21:56
 * @Description:
 */
public class Consumer1 {
    public Consumer1() {
     try {
        String consumerGroup = "message_consumer";
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr("192.168.126.102:9876;192.168.126.103:9876");
        consumer.subscribe("Topic1", "Tag1");
        consumer.registerMessageListener(new Consumer1.Listener1());
        consumer.start();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

class Listener1 implements MessageListenerConcurrently {
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
        new Consumer1();
        System.out.println("consumer1 started");
    }
}
