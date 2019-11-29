package cn.zzrfdsn.cloudlandshop.search.service;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author cloudlandboy
 * @Date 2019/10/20 下午1:14
 * @Since 1.0.0
 */

public class ActiveMQConsumerTest {

    @Test
    public void testQueueConsumer() throws Exception {

        //加载spring容器即可
        ApplicationContext context=new ClassPathXmlApplicationContext("classpath:applicationContext-activemq.xml");

        //等待
        System.in.read();
    }
}