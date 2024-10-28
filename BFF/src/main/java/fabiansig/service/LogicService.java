package fabiansig.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fabiansig.client.ChatGPTClient;
import fabiansig.client.MapsClient;
import fabiansig.dto.IntentionResponse;
import fabiansig.dto.MessageRequest;
import fabiansig.dto.MessageResponse;
import fabiansig.dto.RouteAddressRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogicService {

    private final ChatGPTClient chatGPTClient;
    private final MapsClient mapsClient;

    public String sendMessage(MessageRequest message) {

        IntentionResponse intResponse = chatGPTClient.getIntention(message.content());

    }

}
