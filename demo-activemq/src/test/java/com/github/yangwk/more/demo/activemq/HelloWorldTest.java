package com.github.yangwk.more.demo.activemq;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.yangwk.more.demo.activemq.HelloWorld.HelloWorldConsumer;
import com.github.yangwk.more.demo.activemq.HelloWorld.HelloWorldProducer;

public class HelloWorldTest {
	public static void main(String[] args) throws Exception {
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new HelloWorldProducer());
		executor.execute(new HelloWorldProducer());
		
		Thread.sleep(300);
		
		executor.execute(new HelloWorldConsumer());
		executor.execute(new HelloWorldConsumer());
		
		executor.shutdown();
	}
}
