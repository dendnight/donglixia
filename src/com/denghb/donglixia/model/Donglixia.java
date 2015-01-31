package com.denghb.donglixia.model;

import java.util.ArrayList;

/**
 * 实体类
 * 
 * @author denghb
 *
 */
public class Donglixia {

	/** ID */
	private Integer id;

	/** 标签 */
	private String tag;

	/** 喜欢 */
	private Integer love;

	/** 第一个地址 */
	private String url;

	/** 地址集合 */
	private ArrayList<String> urls = new ArrayList<String>();

	public Donglixia() {
	}

	public Donglixia(Integer id, String tag,Integer love,ArrayList<String> urls) {
		this.id = id;
		this.tag = tag;
		this.love = love;
		
		this.urls.addAll(urls);
	}

	public String getUrl() {
		if (urls.size() > 0) {
			return urls.get(0);
		}
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ArrayList<String> getUrls() {
		return urls;
	}

	public void setUrls(ArrayList<String> urls) {
		this.urls = urls;
	}

	public Integer getLove() {
		return love;
	}

	public void setLove(Integer love) {
		this.love = love;
	}

}
