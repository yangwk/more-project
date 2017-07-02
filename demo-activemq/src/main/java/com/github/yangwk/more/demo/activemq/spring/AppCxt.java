package com.github.yangwk.more.demo.activemq.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.yangwk.more.demo.activemq.spring.service.TopicSendService;

public class AppCxt {
	
	private static ApplicationContext getApplicationContext(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		return ac;
	}
	
	protected static void test(){
		ApplicationContext ac = getApplicationContext();

		MyMessageSender sender = ac.getBean("myMessageSender", MyMessageSender.class);
		sender.send();
	}

	protected static void testTx(){
		ApplicationContext ac = getApplicationContext();
		
		TopicSendService ts = ac.getBean("TopicSendService", TopicSendService.class);
		ts.send("topic.spring.tx", "lala xxoo underage");
	}
	
	public static void main(String[] args) {
//		testTx();
		test();
	}
}
