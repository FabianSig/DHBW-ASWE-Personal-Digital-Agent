package online.dhbw_studentprojekt.bff.client;

import online.dhbw_studentprojekt.dto.news.Article;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface NewsClient {

    @GetExchange("/api/news")
    List<Article> getNews(@RequestParam String topic, @RequestParam int count);
}
