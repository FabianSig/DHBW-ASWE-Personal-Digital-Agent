package nikomitk.mschatgpt.dto.standard;

import lombok.Getter;

@Getter
public class ChatGPTResponseChoice<T> {
    private ChatGPTMessage<T> message;
}
