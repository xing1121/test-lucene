package com.sf.wdx.domain;

/**
 * 描述：歌曲
 * @author 80002888
 * @date   2018年8月13日
 */
public class Song {
	
	/**
	 * 歌名
	 */
	private String songName;
	
	/**
	 * 歌手
	 */
	private String singer;
	
	/**
	 * 作词
	 */
	private String writer;
	
	/**
	 * 作曲
	 */
	private String composer;
	
	/**
	 * 专辑
	 */
	private String album;
	
	/**
	 * 歌词
	 */
	private String lyric;
	
	public Song() {
	}

	public Song(String songName, String singer, String writer, String composer, String album, String lyric) {
		this.songName = songName;
		this.singer = singer;
		this.writer = writer;
		this.composer = composer;
		this.album = album;
		this.lyric = lyric;
	}

	public String getSongName() {
		return songName;
	}

	public void setSongName(String songName) {
		this.songName = songName;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getComposer() {
		return composer;
	}

	public void setComposer(String composer) {
		this.composer = composer;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getLyric() {
		return lyric;
	}

	public void setLyric(String lyric) {
		this.lyric = lyric;
	}
	
	@Override
	public String toString() {
		return "Song [songName=" + songName + ", singer=" + singer + ", writer=" + writer + ", composer=" + composer
				+ ", album=" + album + ", lyric=" + lyric + "]";
	}
	
}
