package com.github.yangwk.more.demo.activemq;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * @author yangwk
 *
 */
public class TopicsTest {
	
	public static void main(String[] args) {
		try {
			//Create a ConnectionFactory
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
		
			//Create a Connection
			Connection connection = connectionFactory.createConnection();
			connection.start();
			
			//Create a Session
			Session session = connection.createSession(false,  Session.AUTO_ACKNOWLEDGE);
			
			//Create the destination (Topic or Queue)
			Destination destination = session.createTopic("one");
			
			//Create a MessageConsumer from the Session to the Topic or Queue
			MessageConsumer consumer = session.createConsumer(destination);
			
			//Wait for a message
			Message message =  consumer.receive(1000);
			
			if(message instanceof TextMessage){
				TextMessage textMessage = (TextMessage)message;
				String text = textMessage.getText();
				System.out.println("Received: " + text);
			}else{
				System.out.println("Received: " + message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
