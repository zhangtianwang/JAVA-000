package com;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class QueueProducer {

    public static void main(String[] args) {
        String userName = "admin";
        String pwd = "admin";
        String url = "tcp://10.6.1.49:61616";
        ConnectionFactory factory = new ActiveMQConnectionFactory(userName, pwd, url);
        Connection conn = null;
        Session session = null;
        Destination dest = null;
        MessageProducer producer;
        try {
            conn = factory.createConnection();
            conn.start();
            session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("test1");
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            for (int i = 1; i <= 10; i++) {
                TextMessage message = session.createTextMessage("Hello,我是数字：" + i);
                producer.send(message);
                session.commit();
            }
        } catch (Exception ex) {
            String msg = ex.getMessage();
        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (JMSException e) {
//                    e.printStackTrace();
//                }
//            }
        }

    }
}
