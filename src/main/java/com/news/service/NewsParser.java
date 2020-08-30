package com.news.service;

import java.util.List;

import org.jsoup.nodes.Document;

import com.news.model.NewsVO;
import com.news.model.WebSite;

public interface NewsParser {
	
	/**
	 * Parser website
	 * @param webSite
	 * @param listeNewsVO
	 * @param doc
	 * @return
	 */
	List<NewsVO> parse(WebSite webSite, Document doc);

}
