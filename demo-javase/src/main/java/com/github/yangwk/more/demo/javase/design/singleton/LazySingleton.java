package com.github.yangwk.more.demo.javase.design.singleton;

public class LazySingleton {

	private static LazySingleton instance = null;

	// 构造方法私有，防止外部进行实例化
	private LazySingleton() {
	}

	public synchronized static LazySingleton getInstance() {
		if (instance == null) {
			instance = new LazySingleton();
		}
		return instance;
	}

}
