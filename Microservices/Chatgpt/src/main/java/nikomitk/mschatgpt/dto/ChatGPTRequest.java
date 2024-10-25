package nikomitk.mschatgpt.dto;

import java.util.List;

public record ChatGPTRequest(String model, List<ChatGPTMessage> messages) {
}
