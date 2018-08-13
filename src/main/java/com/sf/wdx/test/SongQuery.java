package com.sf.wdx.test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.sf.wdx.domain.Song;
import com.sf.wdx.resource.SongResource;

public class SongQuery {
	
	@Test
	public void querySongFromIndexLibrary() throws IOException, ParseException {
		
		//1.读取索引库
		DirectoryReader reader = DirectoryReader.open(FSDirectory.open(new File("E:\\index_song")));
		
		//2.创建IndexSearcher对象
		IndexSearcher indexSearcher = new IndexSearcher(reader);
		
		//3.创建分词器对象，必须和创建索引库时使用的分词器一致
		Analyzer analyzer = new IKAnalyzer();
		
		//4.声明关键词
		String keyWords = "周杰";
		
		//5.创建查询解析器对象：在多个字段中查询
		MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"singer","writer","composer"}, analyzer);
		
		//6.解析关键词
		Query query = parser.parse(keyWords);
		
		//7.执行查询
		TopDocs topDocs = indexSearcher.search(query, 10);
		
		//8.解析显示查询结果
		int totalHits = topDocs.totalHits;
		System.out.println("总命中数="+totalHits);
		
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		for (ScoreDoc scoreDoc : scoreDocs) {
			
			int docid = scoreDoc.doc;
			System.out.println("文档id="+docid);
			
			float score = scoreDoc.score;
			System.out.println("score="+score);
			
			Document document = indexSearcher.doc(docid);
			
			Iterator<IndexableField> iterator = document.iterator();
			
			while (iterator.hasNext()) {
				IndexableField field = (IndexableField) iterator.next();
				
				String name = field.name();
				String stringValue = field.stringValue();
				
				System.out.println(name+"="+stringValue);
				
			}
			
			System.out.println();
			
		}
		
	}
	
	@Test
	public void createSongIndexLibrary() throws IOException {
		
		//1.创建与索引库位置对应的Directory对象
		Directory directory = FSDirectory.open(new File("E:\\index_song"));
		
		//2.创建分词器对象
		Analyzer analyzer = new IKAnalyzer();
		
		//3.封装索引库写入器的配置信息
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
		
		//4.创建索引库写入器
		IndexWriter indexWriter = new IndexWriter(directory, config);
		
		List<Song> songList = SongResource.getSongList();
		
		for (Song song : songList) {
			//5.创建字段域对象
			String songName = song.getSongName();
			String singer = song.getSinger();
			String writer = song.getWriter();
			String composer = song.getComposer();
			String album = song.getAlbum();
			String lyric = song.getLyric();
			
			TextField songNameField = new TextField("songName", songName, Store.YES);
			TextField singerField = new TextField("singer", singer, Store.YES);
			TextField writerField = new TextField("writer", writer, Store.YES);
			TextField composerField = new TextField("composer", composer, Store.YES);
			TextField albumField = new TextField("album", album, Store.YES);
			TextField lyricField = new TextField("lyric", lyric, Store.YES);
			
			//6.创建和各组字段域对象对应的Document对象
			Document document = new Document();
			
			//7.将各组字段域对象添加到对应的Document对象中
			document.add(songNameField);
			document.add(singerField);
			document.add(writerField);
			document.add(composerField);
			document.add(albumField);
			document.add(lyricField);
			
			//8.将各个Document对象添加到IndexWriter对象中
			indexWriter.addDocument(document);
		}
		
		//9.提交写入
		indexWriter.commit();
		
		//10.关闭写入器
		indexWriter.close();
		
	}

}
