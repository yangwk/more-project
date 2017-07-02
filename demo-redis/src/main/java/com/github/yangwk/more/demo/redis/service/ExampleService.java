package com.github.yangwk.more.demo.redis.service;

public interface ExampleService {
	
	void addListData(String key, String value);
	
	void addListData4Exception(String key, String value);
	
	String getData(String key);
	
	void setData(String key,String value);
	
	void setData4Exception(String key,String value);
}
