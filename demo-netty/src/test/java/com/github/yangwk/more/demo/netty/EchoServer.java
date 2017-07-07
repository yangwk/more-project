package com.github.yangwk.more.demo.netty;

public class EchoServer {

	public static void main(String[] args) {
		RunClass.runWithCatch(io.netty.example.echo.EchoServer.class, args);
	}

}
