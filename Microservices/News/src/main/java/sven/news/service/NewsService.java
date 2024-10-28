package sven.news.service;

import org.springframework.stereotype.Service;
import sven.news.client.NewsClient;
import sven.news.dto.News;

import java.util.Optional;

@Service
public class NewsService {
    private final NewsClient newsClient;

    public NewsService(NewsClient newsClient){
        this.newsClient = newsClient;
    }

    public News getNews(Optional<String> newsParam){
        return newsClient.getNews();
    }
}
