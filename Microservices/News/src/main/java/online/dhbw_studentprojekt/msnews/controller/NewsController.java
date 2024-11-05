package online.dhbw_studentprojekt.msnews.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.dhbw_studentprojekt.dto.news.Article;
import online.dhbw_studentprojekt.msnews.service.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/news")
@Slf4j
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Article> getNews(@RequestParam(required = false) String topic, @RequestParam(required = false) Optional<Integer> count) {

        return newsService.getNews(topic, count);
    }

}
