package com.github.yangwk.more.demo.netty;

public class EchoClient {

	public static void main(String[] args) {
		RunClass.runWithCatch(io.netty.example.echo.EchoClient.class, args);
	}

}
