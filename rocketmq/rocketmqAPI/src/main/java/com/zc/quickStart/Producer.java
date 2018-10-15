package com.zc.quickStart;


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
        DefaultMQProducer producer = new DefaultMQProducer("quickStart_producer");
        producer.setNamesrvAddr("192.168.94.11:9876;192.168.94.12:9876;192.168.94.13:9876;192.168.94.14:9876");
        //设置失败重发次数（这设置5次）
        producer.setRetryTimesWhenSendFailed(5);
        producer.start();
        for (int i=0;i<100;i++){
            try {
                Message message = new Message("TopicQuickStart", "TagA", ("Hello RocketMq" + i).getBytes());
               //设置发送消息超时时间单位毫秒(当前设置1秒)
                SendResult sendResult = producer.send(message,1000);
                System.out.println("sendResult:========"+sendResult);
            }catch (Exception e){
                e.printStackTrace();
                Thread.sleep(1000);
            }
        }
        producer.shutdown();
    }
}
