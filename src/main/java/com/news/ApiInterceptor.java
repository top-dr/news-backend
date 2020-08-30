package com.news;


import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

//@Component
public class ApiInterceptor extends HandlerInterceptorAdapter {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
       
		HttpServletRequest myRequest = (HttpServletRequest) request;
				
		String headVal = myRequest.getHeader("java-api-version");
				
		logger.info("***************** Autorization Value = " + headVal);
				
		String url = myRequest.getRequestURI().toString();
				
		if (url.startsWith("/news") && !url.startsWith("/news/search")) {
			
			String withoutNews = url.split("/")[2];
			String newPath = new String(Base64.getDecoder().decode(withoutNews));
			
			String newUrl = "/news/" + newPath;
			
			logger.info("url = " + url);
			
			logger.info("decoded = " + newUrl);
			
			//forward
            request.getRequestDispatcher(newUrl).forward(request,response);
            
            return false;
        }		
		
        return true;
    }

}
