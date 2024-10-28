package nikomitk.mschatgpt.dto;

import java.util.List;
import java.util.Map;

public record ChatGPTIntentionRequest(String model, List<ChatGPTMessage> messages, Map<String, Object> response_format) {
}
