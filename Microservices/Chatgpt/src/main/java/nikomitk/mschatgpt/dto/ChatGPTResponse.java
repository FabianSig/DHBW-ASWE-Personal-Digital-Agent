package nikomitk.mschatgpt.dto;

import java.util.List;

public record ChatGPTResponse(List<ChatGPTResponseChoice> choices) {
}
