package nikomitk.mschatgpt.dto.intention;

import nikomitk.mschatgpt.dto.standard.ChatGPTMessage;

import java.util.List;
import java.util.Map;

public record ChatGPTIntentionRequest(String model, List<ChatGPTMessage<String>> messages, Map<String, Object> response_format) {
}
