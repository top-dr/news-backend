package com.news;

import org.springframework.data.repository.CrudRepository;

import com.news.model.WebSite;

public interface WebSiteRepository extends CrudRepository<WebSite, Long> {
	
	WebSite findByWebId(Long webId);

}
