package com.github.yangwk.more.demo.activemq.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
 
@Component
public class MyMessageSender
{
	@Autowired
    private JmsTemplate jmsTemplate;
 
    public void send()
    {
    	jmsTemplate.setPubSubDomain(true);
    	jmsTemplate.convertAndSend("topic.spring", "i want to fuck your ass");
    }
}