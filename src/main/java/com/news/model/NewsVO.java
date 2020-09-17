package com.news.model;

import java.util.Date;

import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsVO {
	
	private static final Logger log = LoggerFactory.getLogger(NewsVO.class);

	private String title;
	private String img;
	
	private Date dateScrap;
	private String link;
	private String index;
	private boolean last;
	private String content;
	
	private String voiceUrl;
	
	public NewsVO() {
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

	public Date getDateScrap() {
		return dateScrap;
	}

	public void setDateScrap(Date dateSCrap) {
		this.dateScrap = dateSCrap;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getVoiceUrl() {
		return voiceUrl;
	}

	public void setVoiceUrl(String voiceUrl) {
		this.voiceUrl = voiceUrl;
	}

	public boolean isValidNews() {
		if(StringUtil.isBlank(this.content) || StringUtil.isBlank(this.title) 
				|| StringUtil.isBlank(this.link)
				|| StringUtil.isBlank(this.img)) {
			log.error("News parsed is not Valid !!!");
			return false;
		}
		
		return true;
	}

}