package com.atguigu.gmall.payment.test;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;

/**
 * @autor huihui
 * @date 2020/11/14 - 11:00
 */
public class TestMq {
    public static void main(String[] args) {//connection就像一条高速路，而session就是一个高速路货车，每一次提交就是一个session。
        ConnectionFactory connect = new ActiveMQConnectionFactory("tcp://192.168.147.132:61616");
        try {
            Connection connection = connect.createConnection();
            connection.start();
            //第一个值表示是否使用事务，如果选择true，第二个值相当于选择0
            Session session = connection.createSession(true, Session.SESSION_TRANSACTED);//开启事务
            Queue testqueue = session.createQueue("drink");//队列模式的消息
//			Topic t = session.createTopic("TEST2");//话题模式的消息

            MessageProducer producer = session.createProducer(testqueue);
            TextMessage textMessage=new ActiveMQTextMessage();
            textMessage.setText("我渴了，想喝水！！");
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);//消息的持久化
            producer.send(textMessage);
            session.commit();//提交事务
            connection.close();//关闭连接
            System.out.println("成功");

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
