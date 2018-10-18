package com.zc.transaction;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.concurrent.*;

/**
 * @Auther: zhouchao
 * @Date: 2018/10/17 14:57
 * @Description:
 */
public class Producer {
    public static void main(String[] args) throws  InterruptedException {
        try {
            String group_name = "transaction_producer";
            TransactionMQProducer producer = new TransactionMQProducer(group_name);
            producer.setNamesrvAddr("192.168.94.11:9876;192.168.94.12:9876;192.168.94.13:9876;192.168.94.14:9876");
            ExecutorService executorService=new ThreadPoolExecutor (
                            2,
                        5,
                         100,
                         TimeUnit.SECONDS,
                         new ArrayBlockingQueue<Runnable>(2000),
                            new ThreadFactory() {
                                @Override
                                public Thread newThread(Runnable runnable) {
                                    Thread thread = new Thread(runnable);
                                    thread.setName("client-transaction-msg-check-thread");
                                    return thread;
                                }
                            }
                    );
            producer.setExecutorService(executorService);
            producer.setTransactionListener(new TransactionListenerImpl());
            producer.start();
            String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};
            for (int i = 0; i < 10; i++) {
                try {
                    Message msg =
                            new Message("TopicTransaction", tags[i % tags.length], "KEY" + i,
                                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                    SendResult sendResult = producer.sendMessageInTransaction(msg, null);
                    System.out.printf("%s%n", sendResult);
                    Thread.sleep(10);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < 100000; i++) {
                Thread.sleep(1000);
            }
            producer.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
            Thread.sleep(1000);
        }
    }
}
