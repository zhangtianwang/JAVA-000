package com;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class QueueConsumer {

    public static void main(String[] args) throws Exception {
        //创建连接工厂
        String userName = "admin";
        String pwd = "admin";
        String url = "tcp://10.6.1.49:61616";
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(userName, pwd, url);
        //通过连接工厂获取连接
        Connection connection = connectionFactory.createConnection();
        //启动连接
        connection.start();
        //创建会话
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建队列
        Queue queue = session.createQueue("tianwang");
        //创建消费者
        MessageConsumer consumer = session.createConsumer(queue);
        while (true) {
            TextMessage message = (TextMessage) consumer.receive(1000);
            if (message == null) continue;
            System.out.print("消息体：" + message.getText());
        }
    }

}
