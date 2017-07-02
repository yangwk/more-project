package com.github.yangwk.more.demo.redis;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.yangwk.more.demo.redis.service.ExampleService;

public class AppCxt2 {
	
	private static ApplicationContext getApplicationContext(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext2.xml");
		return ac;
	}
	
	protected static void test(){
		ApplicationContext ac = getApplicationContext();
		ExampleService es = ac.getBean("ExampleService", ExampleService.class);
		String key = "value.iloveyou";
		String value = es.getData(key);
		if(value == null){
			es.setData(key, "i want to fuck you girl");
			value = es.getData(key);
		}
		System.out.println(value);
	}

	protected static void testTx(){
		ApplicationContext ac = getApplicationContext();
		ExampleService es = ac.getBean("ExampleService", ExampleService.class);
		String key = "value.iloveyou2";
		String value = es.getData(key);
		if(value == null){
			System.out.println("没有键值");
			es.setData4Exception(key, "i want to fuck you girl very much");
			value = es.getData(key);
		}
		System.out.println(value);
	}
	
	public static void main(String[] args) {
//		test();
		testTx();
	}
}
