package Chaligula.news.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import Chaligula.news.dto.News;
import Chaligula.news.service.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@CrossOrigin(origins="*")
@RequestMapping("/api/news")
@Slf4j
public class NewsController {

    private final NewsService newsService;
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public News getNews(){
        return newsService.getNews();
    }
    
}
