package com.github.yangwk.more.demo.netty;

public class FactorialClient {

	public static void main(String[] args) {
		RunClass.runWithCatch(io.netty.example.factorial.FactorialClient.class, args);
	}

}
