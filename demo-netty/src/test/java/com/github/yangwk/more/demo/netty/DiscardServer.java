package com.github.yangwk.more.demo.netty;

public class DiscardServer {

	public static void main(String[] args) {
		RunClass.runWithCatch(io.netty.example.discard.DiscardServer.class, args);
	}

}
