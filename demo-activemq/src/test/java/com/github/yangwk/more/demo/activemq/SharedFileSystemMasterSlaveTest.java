package com.github.yangwk.more.demo.activemq;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.yangwk.more.demo.activemq.masterslave.SharedFileSystemMasterSlave.Consumer;
import com.github.yangwk.more.demo.activemq.masterslave.SharedFileSystemMasterSlave.Producer;

public class SharedFileSystemMasterSlaveTest {

	public static void main(String[] args) {
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute( new Producer());
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		executor.execute(new Consumer());
		
		executor.shutdown();
		

	}
	

}
