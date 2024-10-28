package nikomitk.mschatgpt.dto.standard;

import java.util.List;

public record ChatGPTRequest(String model, List<ChatGPTMessage<String>> messages) {
}
