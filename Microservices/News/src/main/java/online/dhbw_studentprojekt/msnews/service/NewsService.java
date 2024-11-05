package online.dhbw_studentprojekt.msnews.service;

import lombok.RequiredArgsConstructor;
import online.dhbw_studentprojekt.dto.news.Article;
import online.dhbw_studentprojekt.msnews.client.NewsClient;
import online.dhbw_studentprojekt.msnews.dto.News;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsClient newsClient;


    public List<Article> getNews(String topic, Optional<Integer> count) {

        News news = newsClient.getNews(topic);
        return news.articles().subList(0, Math.min(count.orElse(100), news.articles().size()));
    }

}
