package com.github.yangwk.more.demo.javase.design.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyInvocationHandler implements InvocationHandler {

	private Object proxyObject;
	
	private Logger log = LoggerFactory.getLogger(getClass());

	public MyInvocationHandler(Object proxyObject) {
		this.proxyObject = proxyObject;
	}

	// 在代理实例上处理方法调用并返回结果。在与方法关联的代理实例上调用方法时，将在调用处理程序上调用此方法。
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		log.debug(method.getName()+"()方法被调用前:");
		
		// args 如果接口方法不使用参数，则为 null。
		if (args != null) { // 有入参
			log.debug(args[0].toString() );	//world
			log.debug(args[0].getClass().getName());	//java.lang.String
			boolean isStr = args[0] instanceof String;
			log.debug( String.valueOf(isStr) );	//true
			for (int i = 0; i < args.length; i++) {
				String s = args[i].toString();
				if (s.equals("world")) {
					args[i] = "动态代理处理";	//把参数修改,实现功能修改
				}
			}
		}
		log.debug(proxy.getClass().toString()); // class $Proxy0
		boolean boolPro = proxyObject.equals(proxy);
		log.debug( String.valueOf(boolPro)); // false
		boolPro = proxyObject == proxy;
		log.debug(String.valueOf(boolPro)); // false

		log.debug(proxyObject.getClass().toString()); // class org.proxy.demo.Say_Impl
		boolPro = proxyObject instanceof SayDAO;
		log.debug(String.valueOf(boolPro)); // true
		log.debug(proxyObject.getClass().getInterfaces()[0].toString()); // interface org.ywk.proxy.Say
		
		log.debug(method.getName() );	//sayHello
		
		log.debug(method.getName()+"()方法正在调用...");
		Object methodReturn = method.invoke(proxyObject, args); // 相当于proxyObject.method(args);
		
		log.debug(method.getName()+"()方法被调用后:");
		
		log.debug("我还可以往"+method.getName()+"()方法添加自己的代码哦,是不是很方便");
		log.debug("好吧,我干脆再调用一次"+method.getName()+"()方法吧");
		
		methodReturn = method.invoke(proxyObject, args); // 相当于proxyObject.method(args);
		
		return methodReturn;
	}

}
