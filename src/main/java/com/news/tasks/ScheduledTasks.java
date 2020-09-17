package com.news.tasks;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.news.service.NewsService;

@Component
public class ScheduledTasks {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	@Autowired
	NewsService newsService;
	
	@Scheduled(fixedRate = 300000)
	public void executeTask() {
		log.info("M1 The time is now {}", dateFormat.format(new Date()) + "Current Thread :"+ Thread.currentThread().getName());
		
		try {
			newsService.scrap();
		} catch (IOException e) {
			
			log.error("Exception : " + e);
		}
	}
	
}