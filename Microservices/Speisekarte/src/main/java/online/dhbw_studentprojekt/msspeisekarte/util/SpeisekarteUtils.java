package online.dhbw_studentprojekt.msspeisekarte.util;

import lombok.experimental.UtilityClass;
import online.dhbw_studentprojekt.dto.speisekarte.Speisekarte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class SpeisekarteUtils {

    public Speisekarte extractMenu(String html) {
        String[] gruppen = html.split("<div class='col-xs-4 gruppenname'>");
        return new Speisekarte(
                extractGruppe(gruppen[1]),
                extractGruppe(gruppen[2]),
                extractGruppe(gruppen[3]),
                extractGruppe(gruppen[4]),
                extractGruppe(gruppen[5]),
                extractGruppe(gruppen[6]),
                extractGruppe(gruppen[7]));
    }

    private List<Speisekarte.Speise> extractGruppe(String gruppe) {
        final String[] split = gruppe.split("<span style='font-size:1.5em'>");
        return Arrays.stream(split)
                .dropWhile(s -> s.contains("<div class='col-xs-8 preise-head'>"))
                .map(SpeisekarteUtils::extractSpeise)
                .toList();
    }

    private Speisekarte.Speise extractSpeise(String s) {
        String[] split1 = s.split("</span>", 2);
        String name = split1[0];
        String[] split2 = split1[1].split("<div class='azn hidden size-13'>", 2)[1].replaceFirst("<br>", "").trim().split("<span style='text-decoration:underline'>Portion</span>", 2);

        List<String> allergene = extractAllergene(split2[0]);

        List<Speisekarte.Speise.Naehrwerte> naehrwerte = extractNaehrwerte(split2[1]);

        return new Speisekarte.Speise(name, allergene, naehrwerte);
    }

    private List<String> extractAllergene(String s) {
        if(s.indexOf("<div") < 1) {
            return List.of();
        }

        String allergene = s
                .substring(1, s.indexOf("<div"))
                .trim();

        String[] allergeneArr = allergene.substring(0, allergene.length() - 2)
                .split(", ");

        return List.of(allergeneArr);
    }

    private List<Speisekarte.Speise.Naehrwerte> extractNaehrwerte(String s) {
        s = s.substring(s.indexOf("Brennwert"), s.indexOf("</div>"));

        List<Speisekarte.Speise.Naehrwerte> naehrwerte = new ArrayList<>();

        Arrays.stream(s.split("<br>"))
                .map(String::trim)
                .filter(element -> !element.startsWith("-"))
                .map(element -> element.split(": "))
                .map(element -> new Speisekarte.Speise.Naehrwerte(element[0], element[1]))
                .forEachOrdered(naehrwerte::add);

        return naehrwerte;
    }
}
