package nikomitk.mschatgpt.dto.standard;

import lombok.Getter;

import java.util.List;

@Getter
public class ChatGPTResponse<T> {
    private List<ChatGPTResponseChoice<T>> choices;
}
