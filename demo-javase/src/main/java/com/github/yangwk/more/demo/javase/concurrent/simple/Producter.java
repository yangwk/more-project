package com.github.yangwk.more.demo.javase.concurrent.simple;

public class Producter implements Runnable {

	Resource sto = null;

	public Producter(Resource sto) {
		this.sto = sto;
	}

	@Override
	public void run() {
		while (true) {
			sto.producter();
			try {
				Thread.sleep(10);	//休眠可降低cpu使用率
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

}
