package com.github.yangwk.more.demo.lucene.crawl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import com.github.yangwk.more.demo.lucene.entity.News;


public class NTES {
	
	static final String NEWS_URL = "http://news.163.com/";
	
	/**
	 * 抓取新闻
	 */
	public static List<News> crawl163LatestNews(String category) throws IOException, ParseException{
		
		Whitelist.simpleText();
		//加载文档
		Document doc = Jsoup.connect(NEWS_URL+category).get();
		
		Elements es = doc.getElementsByClass("today_news").first().getElementsByTag("li");
		
		List<News> list=new ArrayList<News>();
		
		for(Element element:es){
			Element a = element.getElementsByTag("a").last();
			String title = a.text();
			String url = a.attr("href");
			String content = null;
			{
				Document contentDoc = Jsoup.connect(url).get();
				//新闻详情
				Element cont = contentDoc.getElementById("endText");
				if(null != cont){
					//移除广告标签
					cont.getElementsByAttributeValueContaining("class", "gg200x300").remove();
					content = cont.text();
				}else{
					content = contentDoc.getElementsByTag("body").text();
				}
			}
			
			News news=new News();
			news.setTitle(title);
			news.setUrl(url);
			news.setContent(content);
			news.setShortContent( content != null ? 
					content.substring(0, Math.min(50, content.length())) : null );
			
			list.add(news);
		}
		return list;
		
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		for(News news:NTES.crawl163LatestNews("shehui")){
			System.out.println("----------------------------------------------------------------------------------------------------------");
			System.out.println(news.getContent());
			System.out.println(news.getDate());
			System.out.println(news.getShortContent());
			System.out.println(news.getTitle());
			System.out.println(news.getUrl());
		}
	}

}
