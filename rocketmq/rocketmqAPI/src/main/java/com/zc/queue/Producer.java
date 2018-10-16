package com.zc.queue;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Auther: zhouchao
 * @Date: 2018/10/16 14:01
 * @Description: *********************************************************
 * Producer，发送顺序消息
 * rocketmq的顺序消息需要满足2点
 * 1、Producer端保证发送消息有序，且发送到同一队列；
 * 2、consumer端保证消费同一队列
 * *********************************************************
 */
public class Producer {
    public static void main(String[] args) throws MQClientException, InterruptedException {
        try {
            String producerGroup = "order_producer";
            DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
            producer.setNamesrvAddr("192.168.94.11:9876;192.168.94.12:9876;192.168.94.13:9876;192.168.94.14:9876");
            producer.start();
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHLmm:ss");
            String dateStr = sdf.format(date);
            for (int i = 1; i <= 5; i++) {
                String body = dateStr + "order_1" + i;
                Message message = new Message("TopicTest", "order_1", "KEY" + i, body.getBytes());
                SendResult sendResult = producer.send(message, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object args) {
                        Integer id=(Integer) args;

                        return mqs.get(id);
                    }
                },0);
                System.out.println(sendResult+",body: "+body);
            }
            for (int i = 1; i <= 5; i++) {
                String body = dateStr + "order_2" + i;
                Message message = new Message("TopicTest", "order_2", "KEY" + i, body.getBytes());
                SendResult sendResult = producer.send(message, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object args) {
                        Integer id=(Integer) args;

                        return mqs.get(id);
                    }
                },1);
                System.out.println(sendResult+",body: "+body);
            }
            for (int i = 1; i <= 5; i++) {
                String body = dateStr + "order_3" + i;
                Message message = new Message("TopicTest", "order_3", "KEY" + i, body.getBytes());
                SendResult sendResult = producer.send(message, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object args) {
                        Integer id=(Integer) args;

                        return mqs.get(id);
                    }
                },3);
                System.out.println(sendResult+",body: "+body);
            }
            producer.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
            Thread.sleep(1000);
        }
    }
}
