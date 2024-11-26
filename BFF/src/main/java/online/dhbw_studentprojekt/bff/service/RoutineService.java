package online.dhbw_studentprojekt.bff.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.dhbw_studentprojekt.bff.client.*;
import online.dhbw_studentprojekt.dto.chatgpt.morning.MorningRequest;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTResponseChoice;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatId;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatMessageRequest;
import online.dhbw_studentprojekt.dto.news.Article;
import online.dhbw_studentprojekt.dto.prefs.Preference;
import online.dhbw_studentprojekt.dto.routing.custom.DirectionResponse;
import online.dhbw_studentprojekt.dto.routing.custom.RouteAddressRequest;
import online.dhbw_studentprojekt.dto.speisekarte.Speisekarte;
import online.dhbw_studentprojekt.dto.stock.Stock;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.*;
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
    private final MapsClient mapsClient;
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
        String newsTopic = prefsClient.getPreference("news-topic")
                .map(pref -> pref.value().getFirst())
                .orElse("");

        List<String> stockSymbols = prefsClient.getPreference("stock")
                .map(Preference::value)
                .orElse(List.of("ALIZF", "GOOGL", "MSFT"));
        // Get news
        List<String> newsHeadlines = new java.util.ArrayList<>(newsClient.getNews(newsTopic, 3).stream().map(Article::title).toList());

        // Get stocks
        List<Stock> stocks = stockClient.getMultipleStock(stockSymbols);
        String prompt = "Du bist ein hilfreicher assistent, der als begleitung eines weckers informationen zu den heutigen nachrichten und bestimmten börsenwerten gibt. Die top 3 Nachrichten heute sind und  aktienwerte sind angehangen";

        ChatMessageRequest chatRequest = new ChatMessageRequest(prompt,
                "Aktienwerte:" + stocks
                        + "News:" + newsHeadlines);
        ChatGPTResponseChoice gptResponse = chatGPTClient.getResponse(chatRequest, ChatId.TEST.getValue(), "message");

        return gptResponse.message().content();
        //TODO For Testing so we dont exceed API limit.
        //return "Heute, am 26. November 2024, hat Verteidigungsminister Boris Pistorius seinen Verzicht auf eine Kanzlerkandidatur erklärt und unterstützt Bundeskanzler Olaf Scholz, der am kommenden Montag offiziell als SPD-Kanzlerkandidat nominiert werden soll. \n" +
        //        "ZDF\n" +
         //       " Zudem hat der Internationale Strafgerichtshof in Den Haag Haftbefehle gegen Israels Premierminister Benjamin Netanjahu und den Hamas-Anführer erlassen. ";
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
            today = today.plusDays(8L - today.getDayOfWeek().getValue());
        }

        List<String> allergene = prefsClient.getPreference("allergene")
                .map(Preference::value)
                .orElse(Collections.emptyList());

        Speisekarte speisekarte = speisekarteClient.getSpeisekarteWithFilteredAllergene(today.toString(), allergene);

        String prompt = "Bitte begrüße mich da es Mittagszeit ist und gebe ein mögliches Menü für das Mittagessen aus. Suche also eine Vorspeise, Hauptspeise und Nachspeise für mich aus.";

        ChatMessageRequest chatRequest = new ChatMessageRequest(prompt,
                "Speisekarte:" + speisekarte);
        ChatGPTResponseChoice gptResponse = chatGPTClient.getResponse(chatRequest, "routine", "message");

        return gptResponse.message().content();
    }

    public String getAbendRoutine(){

        final String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        List<String> home = prefsClient.getPreference("home")
                .map(Preference::value)
                .orElse(Collections.emptyList());

        List<String> work = prefsClient.getPreference("work")
                .map(Preference::value)
                .orElse(Collections.emptyList());

        List<String> travelmode = prefsClient.getPreference("travelMode")
                .map(Preference::value)
                .orElse(Collections.emptyList());

        DirectionResponse directionResponse = mapsClient.getDirections(new RouteAddressRequest(work.getFirst(), home.getFirst(), travelmode.getFirst().toLowerCase()));

        String prompt = "Meine letzte Vorlesung ist zu Ende wünsche mir einen Schönen Feierabend und sage mir wie ich nach Hause komme.";

        ChatMessageRequest chatRequest = new ChatMessageRequest(prompt,
                "time to get there " + directionResponse.routes().getFirst().legs().getFirst().duration().text() + "\n"
                        + "current Time: " + currentTime + "\n"
                        + "additional data about the route: " + directionResponse);

        ChatGPTResponseChoice gptResponse = chatGPTClient.getResponse(chatRequest, "routine", "maps");

        return gptResponse.message().content();
    }

}
