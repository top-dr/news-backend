package com.news;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.news.model.News;

//@CrossOrigin(origins = {"https://jarida24.com","https://www.jarida24.com"}, maxAge = 3600)
@CrossOrigin
@RepositoryRestResource
public interface NewsRepository extends PagingAndSortingRepository<News, Long> { //JpaRepository
	
	Page<News> findByWebSiteIdIn(@Param("ids") Long[] ids, Pageable pageable);
	
	News findByWebSiteIdAndIndex(@Param("webId") Long webId, @Param("index") String index);

}
