package com.github.yangwk.more.demo.activemq.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service("TopicSendService")
public class TopicSendServiceImpl implements TopicSendService{
	
	@Autowired
    private JmsTemplate jmsTemplate;

	@Override
	public void send(String topicName, String message) {
		jmsTemplate.setPubSubDomain(true);
    	jmsTemplate.convertAndSend(topicName, message);
    	if(topicName != null ){
    		throw new RuntimeException("测试事务");
    	}
	}
	
}
