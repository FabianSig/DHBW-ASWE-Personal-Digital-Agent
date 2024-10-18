package fabiansig.dto.geocoding;

import java.util.List;

public record AddressComponent(
        String long_name,
        String short_name,
        List<String> types
) {}
