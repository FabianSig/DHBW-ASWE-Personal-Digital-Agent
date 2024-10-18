package fabiansig.dto.geocoding;

import java.util.List;

public record GeoCodingResponse(
    List<Result> results,
    String status
) {}
