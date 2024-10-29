package nikomitk.mschatgpt.dto.standard;

import java.util.List;


public record ChatGPTResponse(List<ChatGPTResponseChoice> choices) {

}
