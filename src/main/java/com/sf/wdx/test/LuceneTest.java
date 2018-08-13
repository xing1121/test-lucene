package com.sf.wdx.test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class LuceneTest {
	
	@Test
	public void testQueryIndex() throws IOException, ParseException {
		
		//1.读取索引库
		DirectoryReader reader = DirectoryReader.open(FSDirectory.open(new File("E:\\index_en")));
		
		//2.创建索引库核心对象：用于执行搜索操作
		IndexSearcher searcher = new IndexSearcher(reader);
		
		//3.声明关键词
		String keyword = "develop";
		
		//4.创建和“创建索引库”时一致的分词器对象
		Analyzer analyzer = new StandardAnalyzer();
		
		//5.创建查询解析器对象
		QueryParser queryParser = new QueryParser("content", analyzer);
		
		//6.解析关键词
		Query query = queryParser.parse(keyword);
		
		//7.根据query对象从索引库中查询最匹配的10个文档对象
		TopDocs topDocs = searcher.search(query, 10);
		
		//8.获取检索操作的命中数
		int totalHits = topDocs.totalHits;
		System.out.println("totalHits="+totalHits);
		
		//9.获取得分数组
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		
		//10.遍历得分数组
		for (ScoreDoc scoreDoc : scoreDocs) {
			
			//11.获取当前查询结果的文档id
			int docId = scoreDoc.doc;
			System.out.println("docId="+docId);
			
			//12.获取当前查询结果的得分
			float score = scoreDoc.score;
			System.out.println("score="+score);
			
			//13.根据文档id从searcher对象中获取完整的Document对象
			Document document = searcher.doc(docId);
			
			//14.遍历document
			Iterator<IndexableField> iterator = document.iterator();
			while (iterator.hasNext()) {
				IndexableField field = (IndexableField) iterator.next();
				
				//获取字段名称
				String name = field.name();
				
				//获取字段值
				String stringValue = field.stringValue();
				
				System.out.println(name+"="+stringValue);
			}
			
			System.out.println();
		}
		
	}
	
	@Test
	public void testAnalyzer() throws IOException {
		
		//1.创建分词器对象
		//Analyzer analyzer = new StandardAnalyzer();
		//Analyzer analyzer = new CJKAnalyzer();
		//Analyzer analyzer = new SmartChineseAnalyzer();
		Analyzer analyzer = new IKAnalyzer();
		
		//2.调用tokenStream()方法获取tokenStream对象，执行分词
		TokenStream tokenStream = analyzer.tokenStream("content", "I love apple more than banana.相对于香蕉我更喜欢苹果。我爱炒肉丝。Lucy喜欢尚硅谷的前端课程。");
		
		//3.重置指针位置
		tokenStream.reset();
		
		//4.设置分词偏移量引用
		OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
		
		//5.设置分词词语引用
		CharTermAttribute termAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		
		//6.使用类似迭代器的方式遍历tokenStream
		while(tokenStream.incrementToken()) {
			
			//7.获取当前词语的开始和结束位置
			int startOffset = offsetAttribute.startOffset();
			int endOffset = offsetAttribute.endOffset();
			
			//8.打印当前词
			System.out.println(termAttribute+"\t["+startOffset+","+endOffset+"]");
		}
		
		analyzer.close();
	}
	
	@Test
	public void testCreateIndexLibrary() throws IOException {
		
		//1.创建与索引库位置对应的Directory对象
		Directory directory = FSDirectory.open(new File("E:\\index_en"));
		
		//2.创建分词器对象
		Analyzer analyzer = new StandardAnalyzer();
		
		//3.封装索引库写入器的配置信息
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
		
		//4.创建索引库写入器
		IndexWriter indexWriter = new IndexWriter(directory, config);
		
		//5.创建字段域对象
		TextField field01 = new TextField("title", "Java Page", Store.YES);
		TextField field02 = new TextField("content", "Java is a supper develop language.", Store.YES);
		TextField field03 = new TextField("summary", "Java develop language", Store.YES);

		TextField field04 = new TextField("title", "JavaScript Page", Store.YES);
		TextField field05 = new TextField("content", "JavaScript is a good develop language on browser.", Store.YES);
		TextField field06 = new TextField("summary", "JavaScript develop language browser", Store.YES);

		TextField field07 = new TextField("title", "PHP Page", Store.YES);
		TextField field08 = new TextField("content", "PHP is a very stronge develop language on server.", Store.YES);
		TextField field09 = new TextField("summary", "PHP develop language server", Store.YES);
		
		//6.创建和各组字段域对象对应的Document对象
		Document document01 = new Document();
		Document document02 = new Document();
		Document document03 = new Document();
		
		//7.将各组字段域对象添加到对应的Document对象中
		document01.add(field01);
		document01.add(field02);
		document01.add(field03);
		
		document02.add(field04);
		document02.add(field05);
		document02.add(field06);
		
		document03.add(field07);
		document03.add(field08);
		document03.add(field09);
		
		//8.将各个Document对象添加到IndexWriter对象中
		indexWriter.addDocument(document01);
		indexWriter.addDocument(document02);
		indexWriter.addDocument(document03);
		
		//9.提交写入
		indexWriter.commit();
		
		//10.关闭写入器
		indexWriter.close();
	}

}
