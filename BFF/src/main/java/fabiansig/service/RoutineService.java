package fabiansig.service;

import fabiansig.client.ChatGPTClient;
import fabiansig.client.PrefsClient;
import fabiansig.client.SpeisekarteClient;
import fabiansig.client.StockClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.dhbw_studentprojekt.dto.chatgpt.morning.MorningRequest;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTResponseChoice;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatMessageRequest;
import online.dhbw_studentprojekt.dto.speisekarte.Speisekarte;
import online.dhbw_studentprojekt.dto.stock.Stock;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoutineService {


    private final PrefsClient prefsClient;
    private final StockClient stockClient;
    private final ChatGPTClient chatGPTClient;
    private final SpeisekarteClient speisekarteClient;

    public String getMorningRoutine() {
        // Get prefs for news and stocks
        String newsTopic = prefsClient.getPreference("news-topics").value().getFirst();
        List<String> newsHeadlines = List.of("Frieden im nahen Osten", "Mit diesem Trick können Hausbesitzer MILLIONEN sparen!", "Ärzte schockiert: 5 Jähriger junge aus Bietigheim-Bissingen erfindet Krebs-Impfung");


        List<String> stockSymbols = prefsClient.getPreference("stock-symbols").value();

        // Get news

        // Get stocks;
        List<Stock> stocks = stockClient.getMultipleStock(stockSymbols);

        // Get Text for news and stocks
        MorningRequest request = new MorningRequest(newsHeadlines.getFirst(), newsHeadlines.get(1), newsHeadlines.get(2), stocks);
        return chatGPTClient.getMorningRoutine(request).message().content();
    }

    public String getMittagRoutine(){

        LocalDate today = LocalDate.now();

        // Wenn Wochenende, dann auf Montag setzen
        if(today.getDayOfWeek().getValue() > 5) {
            today = today.plusDays(7L - today.getDayOfWeek().getValue());
        }

        List<String> allergene = new ArrayList<>(prefsClient.getPreference("allergene").value());

        Speisekarte speisekarte = speisekarteClient.getSpeisekarteWithFilteredAllergene(today.toString(), allergene);

        String prompt = "Bitte begrüße mich da es Mittagszeit ist und gebe ein Beispiel Menü für das Mittagessen aus.";

        ChatMessageRequest chatRequest = new ChatMessageRequest(prompt,
                "Speisekarte:" + speisekarte);
        ChatGPTResponseChoice gptResponse = chatGPTClient.getResponse(chatRequest, "routine");

        return gptResponse.message().content();
    }
}
