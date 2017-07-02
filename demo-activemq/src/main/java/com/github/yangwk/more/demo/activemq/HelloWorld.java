package com.github.yangwk.more.demo.activemq;

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
 * 该例子不需要启动activemq start<br>
 * 待优化的地方：<br>
 * <pre>
 * 1.Setup a broker instead of using the org.activemq.broker.impl.Main class directly
 * 2.Use JNDI to lookup a javax.jms.ConnectionFactory rather than creating ActiveMQConnectionFactory directly.
 * 3.Implement the javax.jms.MessageListener interface rather than calling consumer.receive()
 * 4.Use transactional sessions
 * 5.Use a Topic rather than a queue
 * </pre>
 * @author yangwk
 *
 */
public class HelloWorld {
	
	public static class HelloWorldProducer implements Runnable{

		@Override
		public void run() {
			try {
				//Create a ConnectionFactory
				ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
				connectionFactory.setSendAcksAsync(true);
				
				//Create a Connection
				Connection connection = connectionFactory.createConnection();
				connection.start();
				
				//Create a Session
				Session session = connection.createSession(false,  Session.AUTO_ACKNOWLEDGE);
				
				//Create the destination (Topic or Queue)
				Destination destination = session.createQueue("TEST.HelloWorld");
				
				//Create a MessageProducer from the Session to the Topic or Queue
				MessageProducer producer = session.createProducer(destination);
				producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
				
				//Create a messages
				String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
				TextMessage message =  session.createTextMessage(text);
				
				//Tell the producer to send the message
				System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
				
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
	
	public static class HelloWorldConsumer implements Runnable, ExceptionListener{

		@Override
		public void run() {
			try {
				//Create a ConnectionFactory
				ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
			
				//Create a Connection
				Connection connection = connectionFactory.createConnection();
				connection.start();
				
				connection.setExceptionListener(this);
				
				//Create a Session
				Session session = connection.createSession(false,  Session.AUTO_ACKNOWLEDGE);
				
				//Create the destination (Topic or Queue)
				Destination destination = session.createQueue("TEST.HelloWorld");
				
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
