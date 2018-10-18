import org.junit.Test;

/**
 * @Auther: zhouchao
 * @Date: 2018/10/18 21:06
 * @Description:AtomicInteger原子操作测试
 */
public class AtomicIntegerTest1 {
    public static int count = 0;

    @Test
    public void test() {
        for (int i = 0; i < 10000; i++) {
            count++;
        }
        System.out.println("count: " + count);
    }
}
