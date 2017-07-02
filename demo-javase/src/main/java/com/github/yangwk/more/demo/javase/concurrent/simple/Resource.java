package com.github.yangwk.more.demo.javase.concurrent.simple;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Resource {
	private Logger log = LoggerFactory.getLogger(getClass());

	private LinkedList<Object> list = null;
	private final int dealCount = 10;

	public Resource() {
		this.list = new LinkedList<Object>();
	}

	public synchronized void producter() {
		log.debug("produting:");
		for (int i = 0; i < dealCount; i++) {
			list.add(i);
		}
		log.debug("producter notifyAll!");
		notify();

		log.debug("producter waiting...");
		try {
			wait();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public synchronized void consumer() {
		if (list.size() == 0) {
			log.debug("consumer waiting...");
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		log.debug("consuming:");
		int size = list.size();
		for (int i = 0; i < size; i++) {
			list.remove();
		}
		log.debug("consumer notifyAll!");
		notify();
	}

}
