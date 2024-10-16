package fabiansig.dto;

public record RouteRequest(
        Origin origin,
        Destination destination,
        String travelMode,
        String routingPreference,
        boolean computeAlternativeRoutes,
        RouteModifiers routeModifiers,
        String languageCode,
        String units
) {}