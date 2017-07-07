package com.github.yangwk.more.demo.netty;

public class TelnetServer {

	public static void main(String[] args) {
		RunClass.runWithCatch(io.netty.example.telnet.TelnetServer.class, args);
	}

}
