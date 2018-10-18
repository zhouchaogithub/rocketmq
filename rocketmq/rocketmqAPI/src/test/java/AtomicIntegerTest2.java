import org.junit.Test;

/**
 * @Auther: zhouchao
 * @Date: 2018/10/18 21:06
 * @Description:AtomicInteger原子操作测试
 */
public class AtomicIntegerTest2 {
    public static int count = 0;
    @Test
    public void test() {
        for (int i = 0; i < 10000; i++) {
            new Thread() {
                public void run() {
                    count++;
                }
            }.start();
        }
        System.out.println("count: " + count);
    }
}
