package com.news.service.websites;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
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
public class HespressParser implements NewsParser {
	
	private static final Logger log = LoggerFactory.getLogger(HespressParser.class);

	@Override
	public List<NewsVO> parse(WebSite webSite, Document doc) {
		
		List<NewsVO> listeNewsVO = new ArrayList<NewsVO>();

		Elements newsHeadlines = doc.select("div.headline_article");
		
		for (Element headline : newsHeadlines) {
			
			String link = headline.child(0).child(0).child(0).attr("abs:href");
			String title = headline.child(0).child(0).child(0).text();
		  	String image = headline.child(0).child(1).child(0).child(0).attr("src");
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
				
				Elements elems =  content.select("#article_body").select("p");
				
				Whitelist wiList = new Whitelist();
				wiList.addTags("p");
				
				String contentBody = Jsoup.clean(elems.outerHtml(), wiList);
				
				// Date ajout				
				String dateAjout = content.select(".story_date").text();	
				LocalDateTime dateAd =  getDate(dateAjout);
							  	
			  	NewsVO newsVO = new NewsVO();
				newsVO.setTitle(title);
				newsVO.setImg(image);
				newsVO.setLink(link);
				newsVO.setIndex(index);
				newsVO.setContent(contentBody);
				newsVO.setDateScrap(dateAd);
				
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
		
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm-dd-M-yyyy", Locale.FRANCE);
		
		
		LocalDateTime dateAd = null;
		
		Date dateAdd = null;
		
		try {
			
			Date today = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(today);
			int mnt = cal.get(Calendar.MONTH) + 1;
			String month = (mnt<10?("0"+mnt):(""+mnt));
			
			//HH:mm-dd-MM-yyyy
			String[] tab = dateAjout.split(" ");
			String dateTime = tab[5] + "-" + tab[1] + "-" + month + "-" + tab[3] ;
			
			dateAdd = formatter.parse(dateTime);
			
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
