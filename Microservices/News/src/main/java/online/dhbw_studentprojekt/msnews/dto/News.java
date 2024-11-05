package online.dhbw_studentprojekt.msnews.dto;

import online.dhbw_studentprojekt.dto.news.Article;

import java.util.List;

public record News(String status, int totalResults, List<Article> articles) {

}



