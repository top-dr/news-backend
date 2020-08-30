package com.news;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.news.model.News;
import com.news.model.PollyUrl;
import com.news.service.PollyService;

@CrossOrigin
@RestController
public class PollyController {
	
	@Autowired
	PollyService pollyService;
	
	@Autowired
	NewsRepository repoNews;
	
	@GetMapping("/polly")
	public PollyUrl greeting(@RequestParam(value = "web") String webId,
			@RequestParam(value = "index") String index){
		
		String url = ""; // "https://ndr-talking-app.s3.eu-west-3.amazonaws.com/1400590.html1586387712831.mp3";
		
		News news = repoNews.findByWebSiteIdAndIndex(Long.parseLong(webId), index);
		String article = news.getArticle();
		
		if(article.length() > 3000) {
			article =  article.substring(0, 3000);
		}
		
		Whitelist wiList = new Whitelist();		
		String contentBody = Jsoup.clean(article, wiList);
		
		//pollyService.toPolly(contentBody, index);
		
		PollyUrl urlPolly = new PollyUrl();
		urlPolly.setUrl(url);
		
		return urlPolly;
	}

}
