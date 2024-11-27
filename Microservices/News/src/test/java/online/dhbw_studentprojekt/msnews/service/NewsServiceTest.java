package online.dhbw_studentprojekt.msnews.service;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import online.dhbw_studentprojekt.dto.news.Article;
import online.dhbw_studentprojekt.msnews.client.NewsClient;
import online.dhbw_studentprojekt.msnews.dto.News;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    @Mock
    private NewsClient newsClient;

    @InjectMocks
    private NewsService newsService;
    @Test
    void getNews_noCount() {
        // Arrange
        Article.Source source = new Article.Source("1", "Test Source");
        Article article1 = new Article(source, "Author1", "Title1", "Description1", "http://url1.com", "http://urlToImage1.com", "2022-01-01T12:00:00Z");
        Article article2 = new Article(source, "Author2", "Title2", "Description2", "http://url2.com", "http://urlToImage2.com", "2022-01-01T12:00:00Z");
        List<Article> articles = List.of(article1, article2);

        News news = new News("ok", 2, articles);
        when(newsClient.getNews("sports")).thenReturn(news);

        // Act
        List<Article> result = newsService.getNews("sports", Optional.empty());

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).title()).isEqualTo("Title1");
        assertThat(result.get(1).title()).isEqualTo("Title2");
        verify(newsClient, times(1)).getNews("sports");
    }

    @Test
    void getNews_withCount() {
        // Arrange
        Article.Source source = new Article.Source("1", "Test Source");
        Article article1 = new Article(source, "Author1", "Title1", "Description1", "http://url1.com", "http://urlToImage1.com", "2022-01-01T12:00:00Z");
        Article article2 = new Article(source, "Author2", "Title2", "Description2", "http://url2.com", "http://urlToImage2.com", "2022-01-01T12:00:00Z");
        List<Article> articles = List.of(article1, article2);

        News news = new News("ok", 2, articles);
        when(newsClient.getNews("sports")).thenReturn(news);

        // Act
        List<Article> result = newsService.getNews("sports", Optional.of(1));

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().title()).isEqualTo("Title1");

        verify(newsClient, times(1)).getNews("sports");
    }
}