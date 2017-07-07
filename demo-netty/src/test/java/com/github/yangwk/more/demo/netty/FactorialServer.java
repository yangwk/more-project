package com.github.yangwk.more.demo.netty;

public class FactorialServer {

	public static void main(String[] args) {
		RunClass.runWithCatch(io.netty.example.factorial.FactorialServer.class, args);
	}

}
