package com.github.yangwk.more.demo.javase.design.proxy;

import java.lang.reflect.Proxy;

public class ProxyFactory {

	/*
	 Foo f = (Foo) Proxy.newProxyInstance(Foo.class.getClassLoader(),
                                          new Class[] { Foo.class },
                                          handler);

	*/
	//创建动态代理,参数:代理类的实例 f
	public static Object createProxy(Object proxyObject){
		Object object = null;
		MyInvocationHandler myInvocationHandler  = new MyInvocationHandler( proxyObject );
		object = Proxy.newProxyInstance(proxyObject.getClass().getClassLoader(), proxyObject.getClass().getInterfaces(), myInvocationHandler);
		return object;
	}
	
}
