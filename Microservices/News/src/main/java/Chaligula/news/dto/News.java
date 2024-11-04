package Chaligula.news.dto;

import java.util.List;

public record News(String status, int totalResults, List<Article> article, List<Source> source) {

    public record Article(String author, String title, String description, String url, String imgURL,
                          String publishDate, String content) {
    }

    public record Source(String id, String name) {
    }
}



