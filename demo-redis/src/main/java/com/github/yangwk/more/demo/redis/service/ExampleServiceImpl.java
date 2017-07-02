package com.github.yangwk.more.demo.redis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * redis cluster不支持事务
 * redis stand-alone支持事务，回滚
 * @author yangwk
 *
 */
@Service("ExampleService")
public class ExampleServiceImpl implements ExampleService{
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public void addListData(String key, String value) {
		System.out.println("开始执行");
		redisTemplate.boundListOps(key).rightPush(value);
		
		List<String> result = redisTemplate.opsForList().range(key, 0, -1);
		for(String s : result){
			System.out.println(s);
		}
		System.out.println("结束执行");
	}

	@Override
	public void addListData4Exception(String key, String value) {
		System.out.println("开始执行");
		List<String> result = redisTemplate.opsForList().range(key, 0, -1);
		for(String s : result){
			System.out.println(s);
		}
		
		redisTemplate.boundListOps(key).rightPush(value);
		if(key != null)
			throw new RuntimeException("抛异常，测试事务");		
		System.out.println("结束执行");
	}

	@Override
	public String getData(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	@Override
	public void setData(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}

	@Override
	public void setData4Exception(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
		if(key != null)
			throw new RuntimeException("抛异常，测试事务");
	}

}
