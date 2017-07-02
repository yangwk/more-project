package com.github.yangwk.more.demo.javase.concurrent.simple;

public class Consumer implements Runnable {

	Resource sto = null;

	public Consumer(Resource sto) {
		this.sto = sto;
	}

	@Override
	public void run() {
		while (true) {
			sto.consumer();
		}
	}

}
