package com.atguigu.gmall.payment.test;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @autor huihui
 * @date 2020/11/14 - 11:04
 */
public class Chenyue {
    public static void main(String[] args) {
        ConnectionFactory connect = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,ActiveMQConnection.DEFAULT_PASSWORD,"tcp://192.168.147.132:61616");
        try {
            Connection connection = connect.createConnection();
            connection.setClientID("zhooidsjfoijsaoifj");
            connection.start();
            //第一个值表示是否使用事务，如果选择true，第二个值相当于选择0
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);//开启事务
            Topic topic = session.createTopic("speaking");
          //  session.createConsumer(topic);
            //将话题消费持久化
            MessageConsumer consumer = session.createDurableSubscriber(topic,"zhooidsjfoijsaoifj");
            consumer.setMessageListener(new MessageListener() {

                public void onMessage(Message message) {
                    if(message instanceof TextMessage){
                        try {
                            String text = ((TextMessage) message).getText();
                            System.err.println(text+"我来倒水给你喝！我是陈悦");

                            //session.rollback();
                        } catch (JMSException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            });


        }catch (Exception e){
            e.printStackTrace();;
        }
    }
}
