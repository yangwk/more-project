package com.github.yangwk.more.demo.activemq.topics;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * @author yangwk
 *
 */
public class TopicsHolder {
	private static final String BROKER_URL = "tcp://127.0.0.1:61616";
	 static final String TOPIC_NAME = "topic.test";
	
	public static class Producer implements Runnable{

		@Override
		public void run() {
			try {
				//Create a ConnectionFactory
				ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
				connectionFactory.setSendAcksAsync(true);
				
				//Create a Connection
				Connection connection = connectionFactory.createConnection();
				connection.start();
				
				//Create a Session
				Session session = connection.createSession(false,  Session.AUTO_ACKNOWLEDGE);
				
				//Create the destination (Topic or Queue)
				Destination destination = session.createTopic(TOPIC_NAME);
				
				//Create a MessageProducer from the Session to the Topic or Queue
				MessageProducer producer = session.createProducer(destination);
				producer.setDeliveryMode(DeliveryMode.PERSISTENT);
				
				//Create a messages
				String text = TOPIC_NAME + " From: " + Thread.currentThread().getName() + " : " + this.hashCode();
				TextMessage message =  session.createTextMessage(text);
				
				//Tell the producer to send the message
				System.out.println("Sent message: "+ message.getText() + " : " + Thread.currentThread().getName());
				
				producer.send(message);
				
				//Clean up
				producer.close();
				session.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		
	}
	
	public static class Consumer implements Runnable, ExceptionListener{

		@Override
		public void run() {
			try {
				//Create a ConnectionFactory
				ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
			
				//Create a Connection
				Connection connection = connectionFactory.createConnection();
				connection.start();
				
				connection.setExceptionListener(this);
				
				//Create a Session
				Session session = connection.createSession(false,  Session.AUTO_ACKNOWLEDGE);
				
				//Create the destination (Topic or Queue)
				Destination destination = session.createTopic(TOPIC_NAME);
				
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

		@Override
		public void onException(JMSException exception) {
			System.out.println("JMS Exception occured.  Shutting down client.");
		}
		
	}

}
