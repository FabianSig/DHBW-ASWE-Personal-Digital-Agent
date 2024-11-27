package online.dhbw_studentprojekt.bff.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.dhbw_studentprojekt.bff.client.*;
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
        // Get preferences from DB
        String newsTopic = prefsClient.getPreference("news-topic")
                .map(pref -> pref.value().getFirst())
                .orElse("");

        List<String> stockSymbols = prefsClient.getPreference("stock")
                .map(Preference::value)
                .orElse(List.of("ALIZF", "GOOGL", "MSFT"));

        // Get news and stocks and taking preferences into account
        List<String> newsHeadlines = new java.util.ArrayList<>(newsClient.getNews(newsTopic, 3).stream().map(Article::title).toList());
        List<Stock> stocks = stockClient.getMultipleStock(stockSymbols);

        String prompt = "Du bist ein hilfreicher assistent, der als begleitung eines weckers informationen zu den heutigen nachrichten und bestimmten börsenwerten gibt. Die top 3 Nachrichten heute sind und Aktienwerte sind angehangen";

        // Requesting ChatGPT to retrieve user facing message
        ChatMessageRequest chatRequest = new ChatMessageRequest(prompt,
                "Aktienwerte:" + stocks
                        + "News:" + newsHeadlines);
        ChatGPTResponseChoice gptResponse = chatGPTClient.getResponse(chatRequest, ChatId.TEST.getValue(), "message");

        return gptResponse.message().content();
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

        String prompt = "Ich gebe dir anbei meine Speisekarte für heute. Bitte begrüße mich da es Mittagszeit ist und gebe ein mögliches Menü für das Mittagessen aus. Suche also eine Vorspeise, Hauptspeise und Nachspeise für mich aus.";

        ChatMessageRequest chatRequest = new ChatMessageRequest(prompt,
                "Speisekarte:" + speisekarte);
        ChatGPTResponseChoice gptResponse = chatGPTClient.getResponse(chatRequest, "routine", "message");

        return gptResponse.message().content();
    }


    public String getNachmittagRoutine() {

        // Get preferences
        List<String> mailDirectories = prefsClient.getPreference("korb")
                .map(Preference::value)
                .orElse(List.of("INBOX"));

        List<String> phoneContacts = prefsClient.getPreference("contact")
                .map(Preference::value)
                .orElse(List.of());

        // Get mail directories
        Map<String, Integer> unreadInMailDirectories = contactsClient.getUnreadInMultipleDirectories(mailDirectories);

        // Get last call dates
        Map<String, LocalDate> lastCallDates = contactsClient.getLastCallDates(phoneContacts)
                .entrySet().stream()
                .filter(entrySet -> entrySet.getValue().isBefore(LocalDate.now().minusDays(7)))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

        String prompt = "Du bist ein hilfreicher assistent, der nachmittags an ungelesene mails in angegebenen ordnern erinnert. Außerdem sollst du erinnern, die angehängten kontakte mal wieder anzurufen, die schon länger nicht mehr kontaktiert werden.";

        ChatMessageRequest request = new ChatMessageRequest(prompt, "Telefonkontakte: " + lastCallDates + "\nMail ordner: " + unreadInMailDirectories);

        ChatGPTResponseChoice responseChoice = chatGPTClient.getResponse(request, ChatId.TEST.getValue(), "message");

        return responseChoice.message().content();
    }

    public String getAbendRoutine() {

        // Get Preferences
        List<String> home = prefsClient.getPreference("home")
                .map(Preference::value)
                .orElse(Collections.emptyList());

        List<String> work = prefsClient.getPreference("work")
                .map(Preference::value)
                .orElse(Collections.emptyList());

        List<String> travelmode = prefsClient.getPreference("travelMode")
                .map(Preference::value)
                .orElse(Collections.emptyList());

        // Get DirectionsResponse from Google Map and build user facing response message with ChatGPT
        DirectionResponse directionResponse = mapsClient.getDirections(new RouteAddressRequest(work.getFirst(), home.getFirst(), travelmode.getFirst().toLowerCase()));

        String prompt = "Meine letzte Vorlesung ist zu Ende wünsche mir einen Schönen Feierabend und sage mir wie ich nach Hause komme.";

        ChatMessageRequest chatRequest = new ChatMessageRequest(prompt,
                "time to get there " + directionResponse.routes().getFirst().legs().getFirst().duration().text() + "\n"
                        + "departure time" + directionResponse.routes().getFirst().legs().getFirst().departure_time() + "\n"
                        + "additional data about the route: " + directionResponse);

        ChatGPTResponseChoice gptResponse = chatGPTClient.getResponse(chatRequest, "routine", "maps");

        return gptResponse.message().content();
    }

}
