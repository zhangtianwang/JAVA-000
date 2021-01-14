package com;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class TopicSubscriber {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String userName = "admin";
        String pwd = "admin";
        String url = "tcp://10.6.1.49:61616";
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(userName,pwd,url);
        try {
            Connection connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic("myTopic.messages");
            MessageConsumer consumer = session.createConsumer(topic);
            while (true){
               Message message = consumer.receive(1000);
               //接收不到消息，继续接收下面的消息
               if(message==null){
                   continue;
               }
               String msgId = message.getJMSMessageID();
               System.out.print("消息Id："+ msgId);
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
