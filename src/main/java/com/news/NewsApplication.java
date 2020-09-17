package com.news;

import java.io.IOException;
import java.util.List;

import javax.jms.ConnectionFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.news.dao.WebSiteRepository;
import com.news.model.NewsVO;
import com.news.model.WebSite;
import com.news.service.HibapressParser;

@SpringBootApplication
@EnableScheduling
public class NewsApplication {

	private static final Logger log = LoggerFactory.getLogger(NewsApplication.class);
	
	@Autowired
	private HibapressParser hibapressParser;
	
	@Autowired
	WebSiteRepository webRepo;

	public static void main(String[] args) {
		SpringApplication.run(NewsApplication.class, args);
	}
	
	@Bean
	  public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
	                          DefaultJmsListenerContainerFactoryConfigurer configurer) {
	    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
	    // This provides all boot's default to this factory, including the message converter
	    configurer.configure(factory, connectionFactory);
	    // You could still override some of Boot's default if necessary.
	    return factory;
	  }

	  @Bean // Serialize message content to json using TextMessage
	  public MessageConverter jacksonJmsMessageConverter() {
	    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
	    converter.setTargetType(MessageType.TEXT);
	    converter.setTypeIdPropertyName("_type");
	    return converter;
	  }
	
	
	@Bean
	CommandLineRunner start(WebSiteRepository webRepo) {
		
		return args -> {
			
			/** Test **/
//			WebSite webSite = webRepo.findByWebId(4L);
//			
//			Document doc;
//			try {
//				doc = Jsoup.connect(webSite.getWebURL()).get();
//			} catch (IOException e) {
//				log.error("Website : " + webSite.getId() + " Exception  getting document : " + e);
//				return;
//			}
//			
//			List<NewsVO> newsVO = hibapressParser.parse(webSite, doc);
//			
//			System.out.println("**");
			/** Test **/
			
			
//			Document content = null;
//			try {
//				content = Jsoup.connect("https://www.akhbarona.com/economy/294008.html").get();
//			} catch (IOException e) {
//				
//			}
//			
//			Elements elems =  content.select("#article_body").select("p");
//			
//			Whitelist wiList = new Whitelist();
//			wiList.addTags("p");
//			
//			String contentBody = Jsoup.clean(elems.outerHtml(), wiList);
//			
//			// Date ajout				
//			String dateAjout = content.select(".story_date").text();		
//			
//			String[] str = dateAjout.split(" ");
			
//			WebSite web = new WebSite();
//			web.setIndex(null);
//			web.setWebId(1L);
//			web.setWebURL("https://www.hespress.com/");
//			
//			webRepo.save(web);
//			
//			WebSite web2 = new WebSite();
//			
//			web2.setIndex(null);
//			web2.setWebId(2L);
//			web2.setWebURL("https://www.akhbarona.com");
//			
//			webRepo.save(web2);
//			
//			WebSite web3 = new WebSite();
//			web3.setIndex(null);
//			web3.setWebId(3L);
//			web3.setWebURL("https://www.alyaoum24.com");
//			
//			webRepo.save(web3);
			
		};
	}

}
	
