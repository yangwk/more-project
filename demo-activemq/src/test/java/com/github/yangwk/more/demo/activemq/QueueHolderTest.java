package com.github.yangwk.more.demo.activemq;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.yangwk.more.demo.activemq.queue.QueueHolder.Consumer;
import com.github.yangwk.more.demo.activemq.queue.QueueHolder.Producer;

public class QueueHolderTest {
	protected static void testDBexceeded(){
		ExecutorService executor = Executors.newFixedThreadPool(2);
		
		for(int i=0; i<100000; i++){
			executor.execute( new Producer());
		}
		
		executor.shutdown();
	}
	
	protected static void test() throws Exception{
		ExecutorService executor = Executors.newCachedThreadPool();
		
		executor.execute( new Producer());
		
		Thread.sleep(500);
		
		executor.execute(new Consumer());
		
		executor.shutdown();
	}

	public static void main(String[] args) throws Exception {
		testDBexceeded();
	}
}
