package com.news.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
//@JsonIgnoreProperties(ignoreUnknown = true)
public class News {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String title;
	private String img;
	private String datePars;
	
	@Column(columnDefinition="text")
	private String article;
	
	private Date dateAdd;
	private String link;
	
	//constraint unique
	@Column(name="indexweb")
	private String index;
	
	@Column(name="islast")
	private boolean last;
	
	@ManyToOne
	@JoinColumn(name = "siteID")
	private WebSite webSite;
	
	@Column(name="voice_url")
	private String voiceUrl;
	
	private Long webSiteId;

	public News() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public WebSite getWebSite() {
		return webSite;
	}

	public void setWebSite(WebSite webSite) {
		this.webSite = webSite;
	}

	public String getDatePars() {
		return datePars;
	}

	public void setDatePars(String datePars) {
		this.datePars = datePars;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public Date getDateAdd() {
		return dateAdd;
	}

	public void setDateAdd(Date dateAdd) {
		this.dateAdd = dateAdd;
	}

	public Long getWebSiteId() {
		return webSiteId;
	}

	public void setWebSiteId(Long webSiteId) {
		this.webSiteId = webSiteId;
	}

	public String getVoiceUrl() {
		return voiceUrl;
	}

	public void setVoiceUrl(String voiceUrl) {
		this.voiceUrl = voiceUrl;
	}
	
}