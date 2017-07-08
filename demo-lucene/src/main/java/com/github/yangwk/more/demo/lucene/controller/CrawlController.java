package com.github.yangwk.more.demo.lucene.controller;

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.yangwk.more.demo.lucene.crawl.NTES;
import com.github.yangwk.more.demo.lucene.entity.News;

@Controller
@RequestMapping("/crawl")
public class CrawlController {
	
	@Autowired(required = false) IndexWriter indexWriter;

	@ResponseBody
	@RequestMapping("ntes")
	public String ntes(String category) throws Exception{
		List<News> list=NTES.crawl163LatestNews(category);
		
		//重复抓取会重复添加索引
		for (News news : list) {
			Document doc = new Document();
			
			Field title = new StringField("title", news.getTitle(), Field.Store.YES);
//			Field content = new Field("content", news.getContent(),Field.Store.YES, Field.Index.ANALYZED);
			Field content = new TextField("content", news.getContent(),Field.Store.YES);
			Field shortContent = new StringField("shortContent", news.getShortContent(),Field.Store.YES);
			Field url = new StringField("url", news.getUrl(), Field.Store.YES);
			
			doc.add(title);
			doc.add(content);
			doc.add(shortContent);
			doc.add(url);
			
			indexWriter.addDocument(doc);
		}
		indexWriter.commit();
		return JSONObject.toJSONString(list);
	}
	
}
