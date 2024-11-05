package Chaligula.news.service;

import org.springframework.stereotype.Service;
import Chaligula.news.client.NewsClient;
import Chaligula.news.dto.News;


@Service
public class NewsService {
    private final NewsClient newsClient;

    public NewsService(NewsClient newsClient){
        this.newsClient = newsClient;
    }

    public News getNews(){
        return newsClient.getNews();
    }
}
