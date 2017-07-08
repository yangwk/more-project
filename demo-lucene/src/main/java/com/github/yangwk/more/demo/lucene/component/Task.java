package com.github.yangwk.more.demo.lucene.component;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Task {
	
	@Autowired(required = false) IndexWriter indexWriter;
	
	final Logger log = Logger.getLogger(getClass());
	
	/**
	 * 实际中不要频繁的优化索引，隔个十天半个月或者几个月的，因为优化索引很浪费资源
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
	@Scheduled(fixedRate=1*60*1000)
	public void optimize() throws CorruptIndexException, IOException {
		log.info("正在优化索引...");
		//...
		log.info("索引优化结束");
	}
}
