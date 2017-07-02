package com.github.yangwk.more.demo.activemq.spring;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.stereotype.Component;
 
@Component
public class MyMessageListener implements MessageListener
{
	@Override
    public void onMessage( final Message message )
    {
        if ( message instanceof TextMessage )
        {
            final TextMessage textMessage = (TextMessage) message;
            try
            {
            	String text = textMessage.getText();
                System.out.println( text );
            }
            catch (final JMSException e)
            {
                e.printStackTrace();
            }
        }
    }
}