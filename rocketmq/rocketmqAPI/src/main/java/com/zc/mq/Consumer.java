package com.zc.mq;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @Auther: zhouchao
 * @Date: 2018/10/11 10:09
 * @Description:
 * *****************************************************************************************
 * 先启动消费端，再启动服务提供端（先订阅再注册）
 * *****************************************************************************************
 *
 */
public class Consumer {
    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("quickStart_consumer");
        consumer.setNamesrvAddr("192.168.126.102:9876;192.168.126.103:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setConsumeMessageBatchMaxSize(10);
        consumer.subscribe("TopicQuickStart","*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                System.out.println("消息条数："+msgs.size());
               try {
                   for (MessageExt msg:msgs) {
                       String topic = msg.getTopic();
                       String tags = msg.getTags();
                       String body = new String(msg.getBody(), "UTF-8");
                       System.out.println("收到消息：topic："+topic +"tags："+tags+"msg："+body);
                           if("Hello RocketMq4".equals(body)){
                               System.out.println("********************************消息失败开始********************************");
                               System.out.println(msg);
                               System.out.println("********************************消息失败结束********************************");
                               throw  new RuntimeException("********************************模拟发生异常********************************");
                           }
                        }
                   }catch (Exception e){
                   e.printStackTrace();
                   //消息消费失败重试机制，
                   return ConsumeConcurrentlyStatus.RECONSUME_LATER;//失败重试
               }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;//消费成功
            }
        });
        consumer.start();
        System.out.println("consumer Started.");
    }
}
