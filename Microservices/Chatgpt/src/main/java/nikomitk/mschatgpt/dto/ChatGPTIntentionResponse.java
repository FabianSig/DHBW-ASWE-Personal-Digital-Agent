package nikomitk.mschatgpt.dto;

import java.util.List;

public record ChatGPTIntentionResponse(String route, List<ChatGPTIntentionAttribut> attributes) {
}
