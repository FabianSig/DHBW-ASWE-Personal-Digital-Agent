package com.chaligula.news_ms.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.chaligula.news_ms.dto.News;
import com.chaligula.news_ms.service.NewsService;

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
    public News getNews(@io.swagger.v3.oas.annotations.Parameter(example = "dd-MM-yyyy") @RequestParam Optional<String> datum) {
        return newsService.getArticles();
    }
}
