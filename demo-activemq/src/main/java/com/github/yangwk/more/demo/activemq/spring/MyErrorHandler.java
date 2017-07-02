package com.github.yangwk.more.demo.activemq.spring;

import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

@Component
public class MyErrorHandler implements ErrorHandler {

	@Override
	public void handleError(Throwable t) {
		t.printStackTrace();
	}
	
	
}
