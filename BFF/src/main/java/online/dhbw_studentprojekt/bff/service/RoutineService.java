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
import online.dhbw_studentprojekt.dto.routing.geocoding.AddressComponent;
import online.dhbw_studentprojekt.dto.routing.geocoding.GeoCodingResponse;
import online.dhbw_studentprojekt.dto.speisekarte.Speisekarte;
import online.dhbw_studentprojekt.dto.stock.Stock;
import online.dhbw_studentprojekt.dto.wetter.Wetter;
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
    private final WetterClient wetterClient;

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
        // Get news TODO
        //List<String> newsHeadlines = new java.util.ArrayList<>(newsClient.getNews(newsTopic, 3).stream().map(Article::title).toList());
        List<String> newsHeadlines = new ArrayList<>();
        newsHeadlines.add("\"Heute, am 26. November 2024, hat Verteidigungsminister Boris Pistorius seinen Verzicht auf eine Kanzlerkandidatur erklärt und unterstützt Bundeskanzler Olaf Scholz, der am kommenden Montag offiziell als SPD-Kanzlerkandidat nominiert werden soll. \\n\" +\n" +
                "        //        \"ZDF\\n\" +\n" +
                "         //       \" Zudem hat der Internationale Strafgerichtshof in Den Haag Haftbefehle gegen Israels Premierminister Benjamin Netanjahu und d");
        // Get stocks TODO
        //List<Stock> stocks = stockClient.getMultipleStock(stockSymbols);
        List<Stock> stocks = new ArrayList<>();

        Stock stock1 = new Stock(
                "TechCorp",
                new Stock.DataPoint("2024-11-25", "120.50", "118.00", "122.00", "121.00"),
                new Stock.DataPoint("2024-11-24", "119.00", "117.50", "120.80", "118.20")
        );

        Stock stock2 = new Stock(
                "GreenEnergy",
                new Stock.DataPoint("2024-11-25", "55.30", "54.00", "56.20", "55.80"),
                new Stock.DataPoint("2024-11-24", "56.00", "53.90", "56.50", "54.50")
        );

        Stock stock3 = new Stock(
                "RetailCo",
                new Stock.DataPoint("2024-11-25", "78.10", "76.80", "79.50", "78.90"),
                new Stock.DataPoint("2024-11-24", "77.50", "75.60", "78.20", "76.80")
        );

        stocks.add(stock1);
        stocks.add(stock2);
        stocks.add(stock3);

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

        GeoCodingResponse geoCodingResponse = mapsClient.getGeoCoding(home.getFirst());

        String shortName = geoCodingResponse.results().stream()
                .flatMap(result -> result.address_components().stream())
                .filter(component -> component.types().contains("locality"))
                .map(AddressComponent::short_name)
                .findFirst()
                .orElse("Stuttgart");

        Wetter wetter = wetterClient.getWetter(shortName);

        String mainWetter = wetter.weather().getFirst().main();
        DirectionResponse directionResponse;
        String prompt;

        if(mainWetter.equalsIgnoreCase("rain")){
            String newTravelMode = "transit";
            directionResponse = mapsClient.getDirections(new RouteAddressRequest(work.getFirst(), home.getFirst(), newTravelMode));
            String tmp = "Heute regnet es leider deswegen muss ich %s anstatt %s nutzen. Meine letzte Vorlesung ist zu Ende wünsche mir einen Schönen Feierabend und sage mir wie ich nach Hause komme. Und wiederhole, dass es regnet und sage dass wir auf die eben genannten alternative ausweichen müssen";
            prompt = String.format(tmp, newTravelMode, travelmode.getFirst().toLowerCase());
        }
        else{
            directionResponse = mapsClient.getDirections(new RouteAddressRequest(work.getFirst(), home.getFirst(), travelmode.getFirst().toLowerCase()));

            prompt = "Meine letzte Vorlesung ist zu Ende wünsche mir einen Schönen Feierabend und sage mir wie ich nach Hause komme.";
        }


        ChatMessageRequest chatRequest = new ChatMessageRequest(prompt,
                "time to get there " + directionResponse.routes().getFirst().legs().getFirst().duration().text() + "\n"
                        + "departure time" + directionResponse.routes().getFirst().legs().getFirst().departure_time() + "\n"
                        + "additional data about the route: " + directionResponse);

        ChatGPTResponseChoice gptResponse = chatGPTClient.getResponse(chatRequest, "routine", "maps");

        return gptResponse.message().content();
    }

}
