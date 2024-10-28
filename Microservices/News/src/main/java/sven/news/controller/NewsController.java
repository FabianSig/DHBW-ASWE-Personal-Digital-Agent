package sven.news.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sven.news.dto.News;
import sven.news.service.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins="*")
@RequestMapping("/api/news")
@Slf4j
public class NewsController {

    private final NewsService newsService;
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public News getNews(@RequestParam Optional<String> news){
        return newsService.getNews(news);
    }
    
}
