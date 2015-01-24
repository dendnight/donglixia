package com.denghb.donglixia.model;

/**
 * 实体类
 * @author denghb
 *
 */
public class Donglixia {
	
	/** ID */
	private Integer id;
	
	/** 地址 */
	private String url;
	
	/** 标签 */
	private String tag;
	
	/** 喜欢 */
	private Integer love;

	public String getUrl() {
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

	public Integer getLove() {
		return love;
	}

	public void setLove(Integer love) {
		this.love = love;
	}
	
}
