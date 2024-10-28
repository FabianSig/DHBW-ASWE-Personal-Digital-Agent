package nikomitk.mschatgpt.dto.intention;

import java.util.List;

public record ChatGPTIntentionResponse(String route, List<ChatGPTIntentionAttribut> attributes) {
}
