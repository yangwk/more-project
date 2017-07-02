package com.github.yangwk.more.demo.activemq.spring.service;

public interface TopicSendService {
	
	void send(String topicName, String message);
}
