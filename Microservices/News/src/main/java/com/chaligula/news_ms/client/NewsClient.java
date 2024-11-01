package com.chaligula.news_ms.client;

import org.springframework.web.client.RestClient;
import org.springframework.web.service.annotation.PostExchange;
import com.chaligula.news_ms.dto.News;

public interface NewsClient extends RestClient {

    @PostExchange("top-headlines?country=us&category=business&apiKey=")
    News getNews();

} 
    


