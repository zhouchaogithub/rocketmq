package com.zc.mq;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;

/**
 * @Auther: zhouchao
 * @Date: 2018/10/11 09:50
 * @Description:
 */
public class Producer {
    public static void main(String[] args) throws MQClientException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("quickStart_producer");
        producer.setNamesrvAddr("192.168.126.102:9876;192.168.126.103:9876");
        producer.start();
        for (int i=0;i<100;i++){
            try {
                Message message = new Message("TopicQuickStart", "TagB", ("Hello RocketMq" + i).getBytes());
                SendResult sendResult = producer.send(message);
                System.out.println("sendResult:========"+sendResult);
            }catch (Exception e){
                e.printStackTrace();
                Thread.sleep(1000);
            }
        }
        producer.shutdown();
    }
}
