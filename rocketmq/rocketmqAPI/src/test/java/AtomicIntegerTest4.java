import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: zhouchao
 * @Date: 2018/10/18 21:06
 * @Description:AtomicInteger原子操作测试
 */
public class AtomicIntegerTest4 {
    public static AtomicInteger count = new AtomicInteger(0);
    @Test
    public void test() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            new Thread() {
                public void run() {
                    for (int j = 0; j < 100; j++) {
                        count.getAndIncrement();
                        count.incrementAndGet();
                    }
                }
            }.start();
        }
        Thread.sleep(1000);
        System.out.println("count: " + count);
    }
}
