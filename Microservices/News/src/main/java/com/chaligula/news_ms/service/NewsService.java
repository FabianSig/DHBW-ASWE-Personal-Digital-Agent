package com.chaligula.news_ms.service;

import org.springframework.stereotype.Service;
import com.chaligula.news_ms.dto.News;
import lombok.extern.slf4j.Slf4j;




@Service
@Slf4j
public class NewsService{

    public static String getArticles;



    //Calls Articles
    public News getArticles(){
        String url = "top-headlines?country=us&category=business&apiKey=";
        return null;
}
}