package online.dhbw_studentprojekt.bff.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.dhbw_studentprojekt.bff.client.*;
import online.dhbw_studentprojekt.dto.chatgpt.morning.MorningRequest;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTResponseChoice;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatMessageRequest;
import online.dhbw_studentprojekt.dto.news.Article;
import online.dhbw_studentprojekt.dto.prefs.Preference;
import online.dhbw_studentprojekt.dto.speisekarte.Speisekarte;
import online.dhbw_studentprojekt.dto.stock.Stock;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoutineService {


    private final PrefsClient prefsClient;
    private final StockClient stockClient;
    private final ChatGPTClient chatGPTClient;
    private final SpeisekarteClient speisekarteClient;
    private final NewsClient newsClient;

    public String getMorningRoutine() {
        // Get prefs for news and stocks
        String newsTopic = prefsClient.getPreference("news-topics")
                .map(pref -> pref.value().getFirst())
                .orElse("");

        int newsCount = prefsClient.getPreference("news-count")
                .map(preference -> Integer.parseInt(preference.value().getFirst()))
                .orElse(3);

        List<String> stockSymbols = prefsClient.getPreference("stock-symbols")
                .map(Preference::value)
                .orElse(List.of("ALIZF", "GOOGL", "MSFT"));

        // Get news
        List<String> newsHeadlines = new java.util.ArrayList<>(newsClient.getNews(newsTopic, newsCount).stream().map(Article::title).toList());
        newsHeadlines.add(null);
        newsHeadlines.add(null);
        newsHeadlines.add(null);

        // Get stocks
        List<Stock> stocks = stockClient.getMultipleStock(stockSymbols);

        // Get Text for news and stocks
        MorningRequest request = new MorningRequest(newsHeadlines.getFirst(), newsHeadlines.get(1), newsHeadlines.get(2), stocks);
        return chatGPTClient.getMorningRoutine(request).message().content();
    }

    public String getMittagRoutine() {

        LocalDate today = LocalDate.now();

        // Wenn Wochenende, dann auf Montag setzen
        if (today.getDayOfWeek().getValue() > 5) {
            today = today.plusDays(7L - today.getDayOfWeek().getValue());
        }

        List<String> allergene = prefsClient.getPreference("allergene")
                .map(Preference::value)
                .orElse(Collections.emptyList());

        Speisekarte speisekarte = speisekarteClient.getSpeisekarteWithFilteredAllergene(today.toString(), allergene);

        String prompt = "Bitte begrüße mich da es Mittagszeit ist und gebe ein Beispiel Menü für das Mittagessen aus.";

        ChatMessageRequest chatRequest = new ChatMessageRequest(prompt,
                "Speisekarte:" + speisekarte);
        ChatGPTResponseChoice gptResponse = chatGPTClient.getResponse(chatRequest, "routine", "message");

        return gptResponse.message().content();
    }

}
