package com.github.yangwk.more.demo.javase;

import com.github.yangwk.more.demo.javase.swing.atm.window.AtmLoginJFrame;

public class ATMTest {
	
	public static void main(String[] args) {
		new AtmLoginJFrame( true );	//默认使用atm键盘,改为false则不使用
	}
}
