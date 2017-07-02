package com.github.yangwk.more.demo.activemq;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.yangwk.more.demo.activemq.topics.TopicsHolder.Consumer;
import com.github.yangwk.more.demo.activemq.topics.TopicsHolder.Producer;

public class TopicsHolderTest {
	public static void main(String[] args) throws Exception {
		ExecutorService executor = Executors.newCachedThreadPool();
		
		executor.execute( new Producer());
		
		Thread.sleep(500);
		
		executor.execute(new Consumer());
		
		executor.shutdown();
	}
}
