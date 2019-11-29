import cn.zzrfdsn.cloudlandShop.util.jedis.JedisClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author cloudlandboy
 * @Date 2019/10/5 上午11:26
 * @Since 1.0.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-redis.xml")
public class JedisClientTest {

    @Autowired
    private JedisClient jedisClient;

    @Test
    public void test() throws Exception {
        String setResult = jedisClient.set("class", "JedisClientTest");
        System.out.println(setResult);

        String getResult = jedisClient.get("class");
        System.out.println(getResult);
    }
}