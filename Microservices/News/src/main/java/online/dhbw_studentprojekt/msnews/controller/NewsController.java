package online.dhbw_studentprojekt.msnews.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/news")
@Slf4j
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get news articles")
    public List<Article> getNews(@Parameter(description = "Determines the topic of the fetched news. Defaults to 'business'") @RequestParam(required = false) String topic, @Parameter(description = "The amount of headlines that should be returned.") @RequestParam(required = false) Optional<Integer> count) {

        return newsService.getNews(topic, count);
    }

}
