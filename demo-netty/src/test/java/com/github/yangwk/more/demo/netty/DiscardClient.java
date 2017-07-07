package com.github.yangwk.more.demo.netty;

public class DiscardClient {

	public static void main(String[] args) {
		RunClass.runWithCatch(io.netty.example.discard.DiscardClient.class, args);
	}

}
