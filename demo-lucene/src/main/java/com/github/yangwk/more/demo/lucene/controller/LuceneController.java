package com.github.yangwk.more.demo.lucene.controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.alibaba.fastjson.JSONObject;
import com.github.yangwk.more.demo.lucene.entity.News;

@Controller
@RequestMapping("/lucene")
public class LuceneController {
	
	Logger log = Logger.getLogger(getClass());
	
	@Autowired(required = false) IndexWriter indexWriter;
	@Autowired(required = false) IKAnalyzer analyzer;
	
	private static final String STARTTAG="<font color='red'>";
	private static final String ENDTAG="</font>";
	
	@ResponseBody
	@RequestMapping("indexFiles")
	public String indexFiles() throws IOException{
		Directory d=indexWriter.getDirectory();
		String[] fs=d.listAll();
		return JSONObject.toJSONString(fs);
	}
	
	@ResponseBody
	@RequestMapping("deleteIndexes")
	public String deleteIndexes(){
		
		String flag="";
		
		try {
			indexWriter.deleteAll();
			indexWriter.commit();
			flag="suc";
		} catch (IOException e) {
			e.printStackTrace();
			try {
				indexWriter.rollback();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			flag="error";
		}
		
		return flag;
	}
	
	
	@ResponseBody
	@RequestMapping("listIndexed")
	public String listIndexed() throws CorruptIndexException, IOException{
		IndexSearcher indexSearcher=getSearcher();
		int size=indexWriter.maxDoc();
		List<News> list=new ArrayList<News>();
		for(int i=0;i<size;i++){
			News news=new News();
			Document doc=indexSearcher.doc(i);
			
			news.setTitle(doc.get("title"));
			news.setUrl(doc.get("url"));
			
			list.add(news);
		}
		return JSONObject.toJSONString(list);
	}

	@ResponseBody
	@RequestMapping("search")
	public String search(String text) throws ParseException, IOException, InvalidTokenOffsetsException{
        
        IndexSearcher searcher=getSearcher();
        
        QueryParser parser=new MultiFieldQueryParser(Version.LUCENE_48, new String[]{"title","content"}, analyzer);
        
        Query query=parser.parse(text);
        
        TopDocs td=searcher.search(query,10);
        
        ScoreDoc[] sd=td.scoreDocs;
        
        SimpleHTMLFormatter simpleHtmlFormatter=new SimpleHTMLFormatter(STARTTAG,ENDTAG);
        
        Highlighter highlighter=new Highlighter(simpleHtmlFormatter,new QueryScorer(query));
        
		List<News> list=new ArrayList<News>();
		
        for(int i=0;i<sd.length;i++){
        	News news=new News();
        	
        	int docId=sd[i].doc;
        	Document doc=searcher.doc(docId);
        	
        	String title=doc.get("title");
        	TokenStream tokenStream = analyzer.tokenStream("title", new StringReader(title));
			String newtitle=highlighter.getBestFragment(tokenStream, title);
    	
			String content=doc.get("content");
			log.debug(content);
	    	tokenStream=analyzer.tokenStream("content", new StringReader(content));
	    	//maxNumFragments越大，显示越全
	    	int maxNumFragments = 100;
	    	String[] newcontent=highlighter.getBestFragments(tokenStream, content, maxNumFragments);
	    	log.debug(newcontent);
	    	
	    	StringBuilder sb = new StringBuilder();
	    	for(String s : newcontent)
	    		sb.append(s);
	    	
	    	news.setTitle(newtitle==null ? title: newtitle);
	    	news.setContent(sb.toString());
			news.setUrl(doc.get("url"));
			news.setDate(doc.get("date"));
			news.setOther(String.valueOf(docId));
	    	list.add(news);
        	
        }
        return JSONObject.toJSONString(list);
	}
	
	
	private IndexSearcher getSearcher() throws IOException{
		return new IndexSearcher(DirectoryReader.open(indexWriter.getDirectory()));
	}
	
}
