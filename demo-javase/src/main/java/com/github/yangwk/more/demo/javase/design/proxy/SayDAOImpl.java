package com.github.yangwk.more.demo.javase.design.proxy;

public class SayDAOImpl implements SayDAO{

	@Override
	public String sayHello(String result) {
		String s  = "sayHello " + result;
		return s;
	}

}
