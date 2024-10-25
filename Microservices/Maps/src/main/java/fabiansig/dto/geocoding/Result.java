package fabiansig.dto.geocoding;

import java.util.List;

public record Result(
        List<AddressComponent> address_components,
        String formatted_address,
        Geometry geometry,
        String place_id,
        PlusCode plus_code,
        List<String> types
) {}

