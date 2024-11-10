package online.dhbw_studentprojekt.msnews.controller;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import online.dhbw_studentprojekt.dto.news.Article;
import online.dhbw_studentprojekt.msnews.service.NewsService;
import online.dhbw_studentprojekt.msnews.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;

@WebMvcTest(NewsController.class)
@Import(TestSecurityConfig.class)
class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsService newsService;

    @Test
    void getNews_returnsNewsList() throws Exception {
        // Arrange - setting up mock response
        Article.Source source = new Article.Source("1", "Test Source");
        Article article = new Article(source, "Author", "Title", "Description", "http://url.com", "http://urlToImage.com", "2022-01-01T12:00:00Z");
        List<Article> articles = List.of(article);

        when(newsService.getNews(anyString(), any())).thenReturn(articles);

        // Act & Assert - making GET request and verifying the response
        mockMvc.perform(get("/api/news")
                        .param("topic", "sports")
                        .param("count", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].author").value("Author"))
                .andExpect(jsonPath("$[0].title").value("Title"))
                .andExpect(jsonPath("$[0].source.name").value("Test Source"));

        verify(newsService, times(1)).getNews("sports", Optional.of(1));
    }

    @Test
    void getNews_withNoArticles_returnsEmptyList() throws Exception {
        // Arrange
        when(newsService.getNews(anyString(), any())).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/api/news")
                        .param("topic", "business")
                        .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(newsService, times(1)).getNews("business", Optional.of(10));
    }
    /*
    Handle Negative Count input first, then add test
    @Test
    void getNews_withNegativeCount_usesDefaultCount() throws Exception {
        // Arrange
        Article.Source source = new Article.Source("1", "Test Source");
        Article article = new Article(source, "Author", "Title", "Description", "http://url.com", "http://urlToImage.com", "2022-01-01T12:00:00Z");
        List<Article> articles = List.of(article);

        when(newsService.getNews(anyString(), any())).thenReturn(articles);

        // Act & Assert
        mockMvc.perform(get("/api/news")
                        .param("topic", "sports")
                        .param("count", "-1"))  // Invalid negative count
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title"));

        verify(newsService, times(1)).getNews("sports", Optional.of(100)); // Default to 100
    }
    */
}

