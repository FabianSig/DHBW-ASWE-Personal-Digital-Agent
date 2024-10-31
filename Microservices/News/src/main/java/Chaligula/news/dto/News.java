package Chaligula.news.dto;

public record News(String news, Main main) {
    
    public record Main(String news) {
    }
}




