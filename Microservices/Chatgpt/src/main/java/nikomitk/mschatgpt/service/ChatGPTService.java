package nikomitk.mschatgpt.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nikomitk.mschatgpt.client.ChatGPTClient;
import nikomitk.mschatgpt.dto.ChatGPTAudioResponse;
import nikomitk.mschatgpt.dto.*;
import nikomitk.mschatgpt.model.Message;
import nikomitk.mschatgpt.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatGPTService {


    private final MessageRepository messageRepository;
    private final ChatGPTClient chatGPTClient;

    private final String responseFormatJson = """
                {
                  "type": "json_schema",
                  "json_schema": {
                    "name": "intention_response",
                    "schema": {
                      "type": "object",
                      "properties": {
                        "route": { "type": "string" },
                        "attributes": {
                          "type": "array",
                          "items": {
                            "type": "object",
                            "properties": {
                              "name": { "type": "string" },
                              "value": { "type": "string" }
                            },
                            "required": ["name", "value"],
                            "additionalProperties": false
                          }
                        }
                      },
                      "required": ["route", "attributes"],
                      "additionalProperties": false
                    },
                    "strict": true
                  }
                }
                """;

    public ChatGPTResponseChoice sendMessage(Request request, String chatId) {

        List<ChatGPTMessage> messages = new java.util.ArrayList<>(messageRepository.findByChatId(chatId).stream().map(m -> new ChatGPTMessage(m.getRole(), m.getContent())).toList());

        Message newMessage = Message.builder().role("user").content(request.message()).chatId(chatId).build();
        messages.add(new ChatGPTMessage(newMessage.getRole(), newMessage.getContent()));

        ChatGPTRequest chatGPTRequest = new ChatGPTRequest("gpt-4o", messages);
        ChatGPTResponse response = chatGPTClient.sendMessage(chatGPTRequest);
        String chatGPTResponseMessage = response.choices().getFirst().message().content();

        Message responseMessage = Message.builder().role("assistant").content(chatGPTResponseMessage).chatId(chatId).build();
        messageRepository.save(newMessage);
        messageRepository.save(responseMessage);

        return response.choices().getFirst();
    }

    public ChatGPTResponseChoice sendMessage(Request request) {
        String defaultChatId = "default";
        return sendMessage(request, defaultChatId);
    }

    public ChatGPTAudioResponse sendAudio(ChatGPTAudioRequest request) {

        return chatGPTClient.sendAudio(request.file(), request.model(), request.language());
    }

    public String findIntention(ChatGPTMessage message) {

        String intentChatId = "intent";
        List<ChatGPTMessage> messages = new java.util.ArrayList<>(messageRepository.findByChatId(intentChatId).stream().map(m -> new ChatGPTMessage(m.getRole(), m.getContent())).toList());

        messages.add(message);

//        ChatGPTRequest chatGPTRequest = new ChatGPTRequest("gpt-4o", messages);
//        ChatGPTResponse response = chatGPTClient.sendMessage(chatGPTRequest);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseFormat;

        try {
            // Parse JSON string to Map
            responseFormat = objectMapper.readValue(responseFormatJson, new TypeReference<>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing response format JSON.";
        }

        ChatGPTIntentionRequest chatGPTRequest = new ChatGPTIntentionRequest("gpt-4o", messages, responseFormat);
        ChatGPTResponse response = chatGPTClient.sendIntentionMessage(chatGPTRequest);


        return response.choices().getFirst().message().content();


    }


}
