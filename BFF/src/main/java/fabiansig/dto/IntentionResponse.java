package fabiansig.dto;

import java.util.List;

public record IntentionResponse(String route, List<IntentionResponseAttribute> attributes) {
}
