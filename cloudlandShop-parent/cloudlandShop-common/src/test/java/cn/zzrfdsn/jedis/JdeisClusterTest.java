package cn.zzrfdsn.jedis;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author cloudlandboy
 * @Date 2019/5/12 上午10:48
 * @Since 1.0.0
 */

public class JdeisClusterTest {

    @Test
    public void test() throws Exception {
        // 第一步：使用JedisCluster对象。需要一个Set<HostAndPort>参数。Redis节点的列表。
        Set<HostAndPort> nodes=new HashSet<HostAndPort>();
        nodes.add(new HostAndPort("172.16.145.130",7001));
        nodes.add(new HostAndPort("172.16.145.130",7002));
        nodes.add(new HostAndPort("172.16.145.130",7003));
        nodes.add(new HostAndPort("172.16.145.130",7004));
        nodes.add(new HostAndPort("172.16.145.130",7005));
        nodes.add(new HostAndPort("172.16.145.130",7006));

        JedisCluster jedisCluster=new JedisCluster(nodes);

        // 第二步：直接使用JedisCluster对象操作redis。在系统中单例存在。
        String setResult = jedisCluster.set("class", "JdeisClusterTest");
        System.out.println(setResult);

        //获取
        String getResult = jedisCluster.get("class");
        System.out.println(getResult);

        //关闭连接
        jedisCluster.close();

    }
}