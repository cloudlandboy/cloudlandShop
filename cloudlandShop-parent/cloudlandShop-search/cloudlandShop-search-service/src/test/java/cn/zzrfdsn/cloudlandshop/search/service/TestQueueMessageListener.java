package cn.zzrfdsn.cloudlandshop.search.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @Author cloudlandboy
 * @Date 2019/10/20 下午1:22
 * @Since 1.0.0
 */

public class TestQueueMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String text = textMessage.getText();
            System.out.println("TestQueueMessageListener 接收到消息：" + text);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}