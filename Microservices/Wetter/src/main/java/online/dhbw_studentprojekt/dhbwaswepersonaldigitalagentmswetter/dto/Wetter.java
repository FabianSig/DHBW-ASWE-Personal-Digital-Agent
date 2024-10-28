package online.dhbw_studentprojekt.dhbwaswepersonaldigitalagentmswetter.dto;

public record Wetter(
        String name,
        Main main
) {
    public record Main(double temp, double feels_like, double temp_min, double temp_max) {
    }

}

