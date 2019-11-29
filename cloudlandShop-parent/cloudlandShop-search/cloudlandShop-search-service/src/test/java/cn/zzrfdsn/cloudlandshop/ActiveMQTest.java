package cn.zzrfdsn.cloudlandshop;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

/**
 * @Author cloudlandboy
 * @Date 2019/10/19 下午3:41
 * @Since 1.0.0
 */

public class ActiveMQTest {

    @Test
    public void testQueueProducer() throws Exception {

        //第一步：创建ConnectionFactory对象，需要指定服务端ip及端口号。
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://172.16.145.133:61616");

        //第二步：使用ConnectionFactory对象创建一个Connection对象。
        Connection connection = connectionFactory.createConnection();

        //第三步：开启连接，调用Connection对象的start方法。
        connection.start();

        //第四步：使用Connection对象创建一个Session对象。
        //第一个参数：是否开启事务。true：开启事务，第二个参数无效，则忽略。
        //第二个参数：当第一个参数为false时，，第二个参数才有意义。消息的应答模式。1、自动应答2、手动应答。一般是自动应答。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //第五步：使用Session对象创建一个Destination（目标）对象（topic、queue），此处创建一个Queue对象。
        //参数：队列的名称。
        Destination queue = session.createQueue("test-queue");

        //第六步：使用Session对象创建一个Producer(消息生产者)对象。
        MessageProducer producer = session.createProducer(queue);

        //第七步：创建一个Message对象，创建一个TextMessage对象。
        /**
         * 第一种方法
         * TextMessage message=new ActiveMQTextMessage();
         * message.setText("hello activeMQ");
         */
        //第二种方法
        TextMessage message = session.createTextMessage("hello activeMQ22222");

        //第八步：使用Producer对象发送消息。
        producer.send(message);

        //第九步：关闭资源
        producer.close();
        session.close();
        connection.close();
    }


    @Test
    public void testQueueConsumer() throws Exception {
        //第一步：创建一个ConnectionFactory对象。
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://172.16.145.133:61616");

        //第二步：从ConnectionFactory对象中获得一个Connection对象。
        Connection connection = connectionFactory.createConnection();

        //第三步：开启连接。调用Connection对象的start方法。
        connection.start();

        //第四步：使用Connection对象创建一个Session对象。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //第五步：使用Session对象创建一个Destination对象。和发送端保持一致queue，并且队列的名称一致。
        Destination queue = session.createQueue("test-queue");

        //第六步：使用Session对象创建一个Consumer对象。
        MessageConsumer consumer = session.createConsumer(queue);

        //第七步：接收消息。
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                //第八步：打印消息。
                TextMessage textMessage = (TextMessage) message;
                try {
                    String text = textMessage.getText();
                    System.out.println("接收到消息：" + text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        //为了测试使用IO阻塞，等待消息
        System.in.read();
        //第九步：关闭资源
        consumer.close();
        session.close();
        connection.close();
    }


    @Test
    public void testTopicProducer() throws Exception {
        //第一步：创建ConnectionFactory对象，需要指定服务端ip及端口号。
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://172.16.145.133:61616");

        //第二步：使用ConnectionFactory对象创建一个Connection对象。
        Connection connection = connectionFactory.createConnection();

        //第三步：开启连接，调用Connection对象的start方法。
        connection.start();

        //第四步：使用Connection对象创建一个Session对象。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //第五步：使用Session对象创建一个Destination对象（topic、queue），此处创建一个Topic对象。
        Destination topic = session.createTopic("test-topic");

        //第六步：使用Session对象创建一个Producer对象。
        MessageProducer producer = session.createProducer(topic);

        //第七步：创建一个Message对象，创建一个TextMessage对象。
        TextMessage textMessage = session.createTextMessage("hello activeMQ-topic-20191019");

        //第八步：使用Producer对象发送消息。
        producer.send(textMessage);

        //第九步：关闭资源
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testTopicConsumer() throws Exception {
        //模拟创建三个消费者
        new Thread(new TopicConsumer()).start();
        new Thread(new TopicConsumer()).start();
        new Thread(new TopicConsumer()).start();
        System.in.read();
    }

    class TopicConsumer implements Runnable {
        @Override
        public void run() {
            try {
                //第一步：创建一个ConnectionFactory对象。
                ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://172.16.145.133:61616");

                //第二步：从ConnectionFactory对象中获得一个Connection对象。
                Connection connection = connectionFactory.createConnection();

                //第三步：开启连接。调用Connection对象的start方法。
                connection.start();

                //第四步：使用Connection对象创建一个Session对象。
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                //第五步：使用Session对象创建一个Destination对象。和发送端保持一致topic，并且话题的名称一致。
                Topic topic = session.createTopic("test-topic");

                //第六步：使用Session对象创建一个Consumer对象。
                MessageConsumer consumer = session.createConsumer(topic);

                //第七步：接收消息。
                consumer.setMessageListener(new MessageListener() {
                    @Override
                    public void onMessage(Message message) {
                        //第八步：打印消息。
                        TextMessage textMessage = (TextMessage) message;
                        try {
                            String text = textMessage.getText();
                            System.out.println(Thread.currentThread().getId() + "接收到消息：" + text);
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                });

                System.in.read();

                //第九步：关闭资源
                consumer.close();
                session.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}