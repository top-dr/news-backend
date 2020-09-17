package com.news.web;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.news.service.NewsService;

@RestController
public class NewsController {
	
	private static final Logger log = LoggerFactory.getLogger(NewsController.class);

	@Autowired
	NewsService newsService;

	@GetMapping("/scrap")
	public void scrap() throws IOException {

		newsService.scrap();
	}

}
