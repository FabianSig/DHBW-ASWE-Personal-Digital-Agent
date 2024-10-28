package nikomitk.mschatgpt.dto.standard;

public record ChatGPTMessage<T>(String role, T payload) {
}
