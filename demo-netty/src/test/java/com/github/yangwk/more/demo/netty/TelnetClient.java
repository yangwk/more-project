package com.github.yangwk.more.demo.netty;

public class TelnetClient {

	public static void main(String[] args) {
		RunClass.runWithCatch(io.netty.example.telnet.TelnetClient.class, args);
	}

}
