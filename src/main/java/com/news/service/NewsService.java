package com.news.service;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.news.dao.NewsRepository;
import com.news.dao.WebSiteRepository;
import com.news.model.News;
import com.news.model.NewsVO;
import com.news.model.WebSite;

@Service
public class NewsService {
	
	private static final Logger log = LoggerFactory.getLogger(NewsService.class);
	
	private static final String baseUrl = "https://ndr-talking-app.s3.eu-west-3.amazonaws.com/";

	@Autowired
	NewsRepository newsRepo;
	
	@Autowired
	WebSiteRepository webRepo;
	
	@Autowired
    private TaskExecutor executor;
	
	@Autowired
	private ParserUtil parser;
	
	@Autowired
	private JmsTemplate jmsTemplate;

	public void scrap() throws IOException {
	
//		Optional<WebSite> site = webRepo.findById(6L);
//		this.scrapWebsite(site.get());
		
		Iterable<WebSite> webSites = webRepo.findAll();
		
		webSites.forEach(site -> {
			try {
				
				this.scrapWebsite(site);
				
			} catch (IOException e) {
				
				log.error("Exception : " +  e);
			}
		} );
		
	}
	
	
	private void scrapWebsite(WebSite webSite) throws IOException {
			
		executor.execute(parseWebSiteRunnable(webSite));			
		
	}
	
	private Runnable parseWebSiteRunnable(WebSite webSite) {
		
		
		return ()-> {
			
			List<NewsVO> listeNewsVO = new ArrayList<NewsVO>();
			
			//LastIndex lastIndex =  indexRepo.findByWebId(webSiteId);
			
			log.info("Website : " + webSite.getId() + " Last Index : "+ webSite.getIndex());
			
			
			Document doc;
			try {
				doc = Jsoup.connect(webSite.getWebURL()).get();
			} catch (IOException e) {
				log.error("Website : " + webSite.getId() + " Exception  getting document : " + e);
				return;
			}
			
			if(doc == null) {
				log.error("Website : " + webSite.getId() + " Document is null");
				return;
			}
			
			log.info("****** Parsed Website :" + webSite.getWebId() + " name : " + webSite.getWebURL());
			
			listeNewsVO = parser.parseWebsite(webSite, doc);
			
			if(listeNewsVO != null && listeNewsVO.size() > 0) {
				
				log.info("Website : " + webSite.getId() + " News Entries Founded :" + listeNewsVO.size());
				
				//manipulate index
				NewsVO lnewsVO = listeNewsVO.get(0);
				lnewsVO.setLast(true);
							
				if(webSite != null) {
					webSite.setIndex(lnewsVO.getIndex());
					webRepo.save(webSite);
				}
			
				for (NewsVO newss : listeNewsVO) {
					
//					String name = newss.getIndex() + String.valueOf(new Date().getTime()) + ".mp3";
//					
//					String url = baseUrl + name;
//					
//					newss.setVoiceUrl(name);
					
					News myNews = new News();
					myNews.setTitle(newss.getTitle());
					myNews.setImg(newss.getImg());
					myNews.setDateAdd(newss.getDateScrap().atZone(ZoneId.of("Europe/London")));
					myNews.setLink(newss.getLink());
					myNews.setIndex(newss.getIndex());
					myNews.setLast(newss.isLast());
					myNews.setWebSite(webSite);
					myNews.setArticle(newss.getContent());
					myNews.setWebSiteId(webSite.getWebId());
					//myNews.setVoiceUrl(url);
					
					newsRepo.save(myNews);
					
					// add before saving object to add url mp3
				//	pollyService.toPolly(newss.getContent(), newss.getIndex());
					
					//add to Queue => News
				    //jmsTemplate.convertAndSend("mailbox", newss);
					
					
				}
			}else {
				log.error("WebSite ID : " + webSite.getId() + "List News Vide !!!!!!");
			}
		};
		
	}

}
