package fabiansig.dto.geocoding;

public record Geometry(
        Location location,
        String location_type,
        Viewport viewport
) {}
