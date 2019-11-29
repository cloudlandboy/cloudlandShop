package cn.zzrfdsn.jedis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Author cloudlandboy
 * @Date 2019/10/5 上午10:25
 * @Since 1.0.0
 */

public class JedisTest {

    @Test
    public void testJedis() throws Exception {
        //1、获得连接对象
        Jedis jedis=new Jedis("172.16.145.130",6379);

        //2、存储
        String setResult = jedis.set("class", "jedisTest");
        System.out.println(setResult);

        //3. 获取
        String getResult = jedis.get("class");
        System.out.println(getResult);

        jedis.close();
    }

    /**
     * 测试jedis连接池
     * @throws Exception
     */
    @Test
    public void testJedisPool() throws Exception {
        JedisPoolConfig config=new JedisPoolConfig();
        //最大闲置个数
        config.setMaxIdle(5);
        //最大连接数
        config.setMaxTotal(10);
        //连接等待时间
        config.setMaxWaitMillis(5000);

        JedisPool jedisPool=new JedisPool(config,"172.16.145.130",6379);

        Jedis jedis = jedisPool.getResource();
        String getResult = jedis.get("class");
        System.out.println(getResult);

        jedis.close();
        jedisPool.close();
    }
}