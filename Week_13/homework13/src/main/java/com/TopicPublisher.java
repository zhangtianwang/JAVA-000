package com;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class TopicPublisher {

    public static void main(String[] args) throws JMSException {
        String userName = "admin";
        String pwd = "admin";
        String url = "tcp://10.6.1.49:61616";
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(userName, pwd, url);
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("myTopic.messages");
        MessageProducer producer = session.createProducer(topic);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        for (int i = 0; i < 10; i++) {
            TextMessage message = session.createTextMessage();
            message.setText("message_" + System.currentTimeMillis());
            producer.send(message);
            System.out.println("Sent message: " + message.getText());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//             session.close();
//             connection.stop();
//             connection.close();
    }

}
