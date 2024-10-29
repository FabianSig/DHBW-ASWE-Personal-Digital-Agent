package nikomitk.mschatgpt.dto.intention;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ChatGPTIntentionResponse {
    private String route;
    private List<ChatGPTIntentionAttribute> attributes;

    @JsonCreator
    public ChatGPTIntentionResponse(@JsonProperty("route") String route, @JsonProperty("attributes") List<ChatGPTIntentionAttribute> attributes) {
        this.route = route;
        this.attributes = attributes;
    }

}
