package com.news.service.websites;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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
public class AkhbaronaParser implements NewsParser {
	
	private static final Logger log = LoggerFactory.getLogger(AkhbaronaParser.class);

	@Override
	public List<NewsVO> parse(WebSite webSite, Document doc) {
		
		List<NewsVO> listeNewsVO = new ArrayList<NewsVO>();

		Elements newsHeadlines = doc.select("#rotating_headlines");
		
		for (Element headline : newsHeadlines.first().children()) {
			
			String link = headline.child(0).child(1).child(0).child(0).attr("abs:href");
			String title = headline.child(0).child(1).child(0).child(0).text();
		  	String image = headline.child(0).child(0).child(0).child(0).attr("src");
		  	String index;
		  	
			if(!StringUtil.isBlank(link) && !StringUtil.isBlank(title)) {
				String[] bits = link.split("/");
			  	index = bits[bits.length-1];
			  	
			  	log.info("Website : " + webSite.getId() + " Load index :" + index);
			  	
			  	if(index != null && index.equals(webSite.getIndex())) {
			  		break;
			  	}
			  	
			  	Document content;
				try {
					content = Jsoup.connect(link).get();
				} catch (IOException e) {
					log.error("Website : " + webSite.getId() + " Exception  getting document : " + e);
					continue;
				}
				
				Elements elems =  content.select("#article_body").select("p");
				
				Whitelist wiList = new Whitelist();
				wiList.addTags("p","br");
				
				String contentBody = Jsoup.clean(elems.outerHtml(), wiList);
				
				// Date ajout				
				String dateAjout = content.select(".story_date").text();	
				LocalDateTime dateAd =  getDate(dateAjout);
			  	
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
		
		String hourAgo = null;	
		String minuteAgo = null;
				 
		LocalDateTime dateAd = null;
		
		try {
			
			if (dateAjout.contains("ساعات") && dateAjout.contains("دقيقة")) {

				String[] str = dateAjout.split(" ");
				hourAgo = str[0];
				minuteAgo = str[2];
			} else if (dateAjout.contains("ساعات")) {

				String[] str = dateAjout.split(" ");
				hourAgo = str[0];
			} else if (dateAjout.contains("دقيقة")) {
				
				String[] str = dateAjout.split(" ");
				minuteAgo = str[0];
			}else if(dateAjout.contains("ساعة واحدة")) {
				hourAgo = "1";
			}
			
			int hour = 0;
			int minute = 0 ;
			
			Instant instant = Instant.now();
			
			if(hourAgo != null) {
				hour = Integer.parseInt(hourAgo);
				instant = instant.minus(hour, ChronoUnit.HOURS);
			}
			
			if(minuteAgo != null) {
				minute = Integer.parseInt(minuteAgo);
				instant = instant.minus(minute, ChronoUnit.MINUTES);
			}
			
			dateAd = LocalDateTime.from(instant);
			
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
