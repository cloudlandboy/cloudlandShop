package cn.zzrfdsn.cloudlandShop.service;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

/**
 * @Author cloudlandboy
 * @Date 2019/10/20 下午12:27
 * @Since 1.0.0
 */

public class ActiveMQProducerTest {

    /**
     * 发送消息
     * @throws Exception
     */
    @Test
    public void testQueueProducer() throws Exception {
        //初始化spring容器
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext-activemq.xml");

        //从spring容器中获得JmsTemplate对象
        JmsTemplate jmsTemplate = (JmsTemplate) context.getBean("jmsTemplate");

        //从spring容器中取Destination对象
        Destination queue = (Destination) context.getBean("queueDestination");
        //使用JmsTemplate对象发送消息。
        jmsTemplate.send(queue,new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                //创建一个消息对象并返回
                TextMessage textMessage = session.createTextMessage("hello spring-activemq-queue");
                return textMessage;
            }
        });
    }
}