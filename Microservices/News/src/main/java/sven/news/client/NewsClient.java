package sven.news.client;

import sven.news.dto.News;
import org.springframework.web.client.RestClient;
import org.springframework.web.service.annotation.PostExchange;


public interface NewsClient extends RestClient {

    @PostExchange("/everything?q=tesla&from=2024-09-28&sortBy=publishedAt&apiKey=hiermussKeyrein")
    News getNews();

}

