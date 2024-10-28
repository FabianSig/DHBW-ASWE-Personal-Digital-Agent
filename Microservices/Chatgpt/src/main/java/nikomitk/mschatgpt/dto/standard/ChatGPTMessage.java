package nikomitk.mschatgpt.dto.standard;

import lombok.Getter;

@Getter
public record ChatGPTMessage<T>(String role, T payload) {
}
