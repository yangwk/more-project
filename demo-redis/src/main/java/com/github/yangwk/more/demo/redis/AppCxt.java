package com.github.yangwk.more.demo.redis;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.yangwk.more.demo.redis.service.ExampleService;

public class AppCxt {
	
	private static ApplicationContext getApplicationContext(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		return ac;
	}
	
	protected static void test(){
		ApplicationContext ac = getApplicationContext();
		ExampleService es = ac.getBean("ExampleService", ExampleService.class);
		es.addListData("list.hello", "i can see you2");
		es.addListData("list.hello", "what is going on2");
	}

	public static void main(String[] args) {
		test();
	}
}
