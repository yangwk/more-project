package com.github.yangwk.more.demo.javase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.yangwk.more.demo.javase.design.singleton.LazySingleton;

public class LazySingletonTest {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Test
	public void test(){
		LazySingleton instance = LazySingleton.getInstance() ;
		LazySingleton instance1 = LazySingleton.getInstance() ;
		boolean b = instance==instance1;
		log.debug(String.valueOf(b)) ; //instance 和 instance1 完全相同时返回true
	}
}
