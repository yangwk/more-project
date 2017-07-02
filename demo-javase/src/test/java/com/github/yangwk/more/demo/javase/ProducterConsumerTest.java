package com.github.yangwk.more.demo.javase;

import com.github.yangwk.more.demo.javase.concurrent.simple.Consumer;
import com.github.yangwk.more.demo.javase.concurrent.simple.Producter;
import com.github.yangwk.more.demo.javase.concurrent.simple.Resource;

public class ProducterConsumerTest {

	public static void main(String[] args) {
		Resource sto = new Resource();
		new Thread(new Producter(sto)).start();
		new Thread(new Consumer(sto)).start();
	}

}
