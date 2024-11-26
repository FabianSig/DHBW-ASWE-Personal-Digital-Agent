package online.dhbw_studentprojekt.msprefs.model;

public record PreferenceForm(
        Transportation transportation,
        Address homeAddress,
        Address workAddress,
        Email email,
        String reminder,
        Alarm alarm,
        Allergens allergens
) {
    public record Transportation(String mode) {}

    public record Address(String address) {}

    public record Email(String korb1, String korb2, String korb3) {}

    public record Alarm(String alarmDate, String alarmTime) {}

    public record Allergens(
            boolean Ei,
            boolean En,
            boolean Fi,
            boolean GID,
            boolean GIG,
            boolean GIH,
            boolean GIKW,
            boolean GIR,
            boolean GIW,
            boolean Kr,
            boolean La,
            boolean Lu,
            boolean NuC,
            boolean NuH,
            boolean NuM,
            boolean NuMa,
            boolean NuPa,
            boolean NuPe,
            boolean NuPi,
            boolean NuW,
            boolean Se,
            boolean Sf,
            boolean Sl,
            boolean So,
            boolean Sw,
            boolean Wt
    ) {}
}

