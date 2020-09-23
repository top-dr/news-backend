package com.news.service.websites;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.news.model.NewsVO;
import com.news.model.WebSite;
import com.news.service.NewsParser;

@Component
public class Alyaoum24Parser implements NewsParser {
	
	private static final Logger log = LoggerFactory.getLogger(Alyaoum24Parser.class);

	@Override
	public List<NewsVO> parse(WebSite webSite, Document doc) {
		
		List<NewsVO> listeNewsVO = new ArrayList<NewsVO>();

		Elements newsHeadlines = doc.select("ul.slider_items li");
		
		for (Element headline : newsHeadlines) {
			
			String link = headline.child(1).attr("abs:href");
			String title = headline.child(2).child(0).text();
		  	String image = headline.child(1).child(0).attr("src");
		  	String index;
		  	
			if(!StringUtil.isBlank(link) && !StringUtil.isBlank(title)) {
				String[] bits = link.split("/");
			  	index = bits[bits.length-1];
			  	
			  	log.info("Website : " + webSite.getId() + " Load index :" + index);
			  	
			  	if(index != null && index.equals(webSite.getIndex())) {
			  		break;
			  	}
			  	
			  	//article content
			  	
			  	Document content;
				try {
					content = Jsoup.connect(link).get();
				} catch (IOException e) {
					log.error("Website : " + webSite.getId() + " Exception  getting document : " + e);
					continue;
				}
				
				Elements elems =  content.select("#post_content").select("p");
				
				Whitelist wiList = new Whitelist();
				wiList.addTags("p");
				
				String contentBody = Jsoup.clean(elems.outerHtml(), wiList);
				
				// Date ajout				
				String dateAjout = content.select("li.time").text();		
				LocalDateTime dateAd = getDate(dateAjout);
				
			  	
			  	NewsVO newsVO = new NewsVO();
				newsVO.setTitle(title);
				newsVO.setImg(image);
				newsVO.setDateScrap(dateAd);
				newsVO.setLink(link);
				newsVO.setIndex(index);
				newsVO.setContent(contentBody);
				
				if(newsVO.isValidNews()) {
					listeNewsVO.add(newsVO);
				}
				
			}
		}
		
		return listeNewsVO;
	}
	
	/**
	 * Get Date from String
	 * @param dateAjout
	 * @return
	 */
	public LocalDateTime getDate(String dateAjout) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.FRANCE);
		 
		LocalDateTime dateAd = null;
		
		try {
			
			Date dateAdd = formatter.parse(dateAjout);
			
			dateAd = dateAdd.toInstant()
				      .atZone(ZoneId.of("Europe/Paris"))
				      .toLocalDateTime();
			
		} catch (Exception e) {
			log.error("Exception Formatting Date : " + e);
		}
		
		if(dateAd != null) {
			return dateAd;
		}else {
			return LocalDateTime.now();
		}
	}

}
