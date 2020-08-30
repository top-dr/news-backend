package com.news.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.news.model.NewsVO;
import com.news.model.WebSite;

@Component
public class ParserUtil {
	
	private static final Long HESPRESS = 1L;
	private static final Long AKHBARONA = 2L;
	private static final Long ALYAOUM24 = 3L;
	
	@Autowired
	private HespressParser hespressPerser;
	
	@Autowired
	private AkhbaronaParser akhbaronaPerser;
	
	@Autowired
	private Alyaoum24Parser alyaoum24Parser;
	

	public List<NewsVO> parseWebsite(WebSite website, Document doc){
		
		List<NewsVO> listNews = new ArrayList<NewsVO>();
		
		if(HESPRESS == website.getWebId()) {
			return hespressPerser.parse(website, doc);
		}else if (AKHBARONA == website.getWebId()) {
			return akhbaronaPerser.parse(website, doc);
		}else if (ALYAOUM24 == website.getWebId()) {
			return alyaoum24Parser.parse(website, doc);
		}
		
		return listNews;
	}
	
}
