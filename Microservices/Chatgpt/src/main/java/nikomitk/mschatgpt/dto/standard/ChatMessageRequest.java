package nikomitk.mschatgpt.dto.standard;

import nikomitk.mschatgpt.dto.intention.ChatGPTIntentionAttribute;

import java.util.List;

public record ChatMessageRequest(String message, String data) {
}
