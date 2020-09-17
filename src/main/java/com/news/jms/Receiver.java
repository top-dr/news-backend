package com.news.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.news.model.NewsVO;
import com.news.service.PollyService;

/**
 * created for batch add urlVoice 
 * 
 * used by jmsTemplate.convertAndSend("mailbox", newss); 
 * @author mac
 *
 */
@Component
public class Receiver {
	
	@Autowired
	private PollyService pollyService;

	@JmsListener(destination = "mailbox", containerFactory = "myFactory")
	  public void receiveMessage(NewsVO news) {
	    System.out.println("************* Begin Received <" + news.getIndex() + ">****************");
	   
		//pollyService.toPolly(news);
	    
	    System.out.println("************* FIN Received <" + news.getIndex() + ">****************");
	  }
}
