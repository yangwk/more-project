package com.github.yangwk.more.demo.javase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.yangwk.more.demo.javase.design.proxy.ProxyFactory;
import com.github.yangwk.more.demo.javase.design.proxy.SayDAO;
import com.github.yangwk.more.demo.javase.design.proxy.SayDAOImpl;

public class ProxyTest {
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Test
	public void test(){
		log.debug("不加动态代理,直接子类实例化");
		SayDAO say = new SayDAOImpl();
		String result = say.sayHello("world");
		log.debug(result);
		
		log.debug("动态代理");
		say = (SayDAO)ProxyFactory.createProxy(say);
		result = say.sayHello("world");
		log.debug(result);
	}
	
}
