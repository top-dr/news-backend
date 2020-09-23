package com.news.service;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.news.model.NewsVO;
import com.news.model.WebSite;
import com.news.service.websites.AkhbaronaParser;
import com.news.service.websites.Alyaoum24Parser;
import com.news.service.websites.BarlamaneParser;
import com.news.service.websites.GoudParser;
import com.news.service.websites.HespressParser;
import com.news.service.websites.HibapressParser;
import com.news.service.websites.Le360Parser;
import com.news.utils.Constantes;

@Component
public class ParserUtil {
	
	@Autowired
	private HespressParser hespressPerser;
	
	@Autowired
	private AkhbaronaParser akhbaronaPerser;
	
	@Autowired
	private Alyaoum24Parser alyaoum24Parser;
	
	@Autowired
	private HibapressParser hibaPressParser;
	
	@Autowired
	private GoudParser goudParser;
	
	@Autowired
	private BarlamaneParser barlamaneParser;
	
	@Autowired
	private Le360Parser le360Parser;

	public List<NewsVO> parseWebsite(WebSite website, Document doc){
		
		List<NewsVO> listNews = new ArrayList<NewsVO>();
		
		switch (website.getWebId().intValue()) {
			
			case Constantes.HESPRESS	:	return hespressPerser.parse(website, doc);
			case Constantes.AKHBARONA	:	return akhbaronaPerser.parse(website, doc);
			case Constantes.ALYAOUM24	:	return alyaoum24Parser.parse(website, doc);
			case Constantes.HIBAPRESS	:	return hibaPressParser.parse(website, doc);
			case Constantes.GOUD 		:	return goudParser.parse(website, doc);
			case Constantes.BARLAMANE 	:	return barlamaneParser.parse(website, doc);
			case Constantes.LE360 		:	return le360Parser.parse(website, doc);

			default: return listNews;
			
		}
		
	}
	
}
