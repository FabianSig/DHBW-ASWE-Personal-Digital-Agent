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
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoutineService {


    private final PrefsClient prefsClient;
    private final StockClient stockClient;
    private final ChatGPTClient chatGPTClient;
    private final SpeisekarteClient speisekarteClient;
    private final NewsClient newsClient;
    private final ContactsClient contactsClient;

    /**
     * Retrieves the morning routine by gathering and processing user preferences for news topics and stock symbols,
     * fetching the current news and stock data, and generating a ChatGPT-based summary.
     *
     * @return A string containing the generated morning routine, which includes summaries of the latest news
     * and stock information.
     */
    public String getMorningRoutine() {
        // Get prefs for news, stocks and contacts
        String newsTopic = prefsClient.getPreference("news-topics")
                .map(pref -> pref.value().getFirst())
                .orElse("");

        int newsCount = prefsClient.getPreference("news-count")
                .map(preference -> Integer.parseInt(preference.value().getFirst()))
                .orElse(3);

        List<String> stockSymbols = prefsClient.getPreference("stock-symbols")
                .map(Preference::value)
                .orElse(List.of("ALIZF", "GOOGL", "MSFT"));


        List<String> mailDirectories = prefsClient.getPreference("mail-directories")
                .map(Preference::value)
                .orElse(List.of("inbox"));

        List<String> phoneContacts = prefsClient.getPreference("phone-contacts")
                .map(Preference::value)
                .orElse(List.of());

        // Get news
        List<String> newsHeadlines = new java.util.ArrayList<>(newsClient.getNews(newsTopic, newsCount).stream().map(Article::title).toList());
        // Bugfix for chatgpt call
        newsHeadlines.add(null);
        newsHeadlines.add(null);
        newsHeadlines.add(null);

        // Get stocks
        List<Stock> stocks = stockClient.getMultipleStock(stockSymbols);

        // Get mail directories
        Map<String, Integer> unreadInMailDirectories = contactsClient.getUnreadInMultipleDirectories(mailDirectories);

        // Get last call dates
        Map<String, LocalDate> lastCallDates = contactsClient.getLastCallDates(phoneContacts)
                .entrySet().stream()
                .filter(entrySet -> entrySet.getValue().isBefore(LocalDate.now().minusDays(7)))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

        // Get Text for news and stocks
        MorningRequest request = new MorningRequest(newsHeadlines.getFirst(), newsHeadlines.get(1), newsHeadlines.get(2), stocks, unreadInMailDirectories, lastCallDates);
        return chatGPTClient.getMorningRoutine(request).message().content();
    }

    /**
     * Retrieves the midday routine by determining the current date and adjusting for weekends,
     * fetching a menu filtered by user-specified allergens, and generating a ChatGPT-based lunch
     * suggestion.
     *
     * @return A string containing the ChatGPT generated message suggesting a lunch menu, including
     * a greeting and menu details.
     */
    public String getMittagRoutine() {

        LocalDate today = LocalDate.now();

        // If weekend, set date to next monday
        if (today.getDayOfWeek().getValue() > 5) {
            today = today.plusDays(7L - today.getDayOfWeek().getValue());
        }

        List<String> allergene = prefsClient.getPreference("allergene")
                .map(Preference::value)
                .orElse(Collections.emptyList());

        Speisekarte speisekarte = speisekarteClient.getSpeisekarteWithFilteredAllergene(today.toString(), allergene);

        String prompt = "Bitte begrüße mich da es Mittagszeit ist und gebe ein mögliches Menü für das Mittagessen aus.";

        ChatMessageRequest chatRequest = new ChatMessageRequest(prompt,
                "Speisekarte:" + speisekarte);
        ChatGPTResponseChoice gptResponse = chatGPTClient.getResponse(chatRequest, "routine", "message");

        return gptResponse.message().content();
    }

}
