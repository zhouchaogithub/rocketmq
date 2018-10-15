package com.zc.model;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * @Auther: zhouchao
 * @Date: 2018/10/11 09:50
 * @Description:
 */
public class Producer {
    public static void main(String[] args) throws MQClientException, InterruptedException {
        String producerGroup="producer1";
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr("192.168.94.11:9876;192.168.94.12:9876;192.168.94.13:9876;192.168.94.14:9876");
        //设置失败重发次数（这设置5次）
        producer.setRetryTimesWhenSendFailed(5);
        producer.start();
        for (int i=0;i<200;i++){
            try {
                Message message = new Message("Topic1", "Tag1", ("消息内容" + i).getBytes());
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
