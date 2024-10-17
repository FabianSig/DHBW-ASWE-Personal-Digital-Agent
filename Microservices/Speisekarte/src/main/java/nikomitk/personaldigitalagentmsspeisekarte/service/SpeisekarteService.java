package nikomitk.personaldigitalagentmsspeisekarte.service;

import lombok.extern.slf4j.Slf4j;
import nikomitk.personaldigitalagentmsspeisekarte.Speisekarte;
import nikomitk.personaldigitalagentmsspeisekarte.client.SpeisekarteClient;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class SpeisekarteService {


    private final SpeisekarteClient speisekarteClient;

    public SpeisekarteService(SpeisekarteClient speisekarteClient) {
        this.speisekarteClient = speisekarteClient;
    }

    private static List<String> extractMenu(String gruppe) {
        String[] split = gruppe.split("<span style='font-size:1.5em'>");
        return Arrays.stream(split)
                .map(s -> s.split("</span>")[0])
                .dropWhile(s -> s.contains("<div"))
                .toList();
    }

    public Speisekarte getSpeisekarte() {
        String today = LocalDate.now().toString();
        String startThisWeek = LocalDate.now().getDayOfWeek().getValue() <= 3 ? LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue()).toString() : LocalDate.now().plusDays(7 - LocalDate.now().getDayOfWeek().getValue()).toString();
        String startNextWeek = LocalDate.now().getDayOfWeek().getValue() <= 3 ? LocalDate.now().plusDays(7L - LocalDate.now().getDayOfWeek().getValue()).toString() : LocalDate.now().plusDays(14 - LocalDate.now().getDayOfWeek().getValue()).toString();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("func", List.of("make_spl"));
        formData.put("locId", List.of("16"));
        formData.put("date", List.of(today));
        formData.put("lang", List.of("de"));
        formData.put("startThisWeek", List.of(startThisWeek));
        formData.put("startNextWeek", List.of(startNextWeek));

        //String websiteHtml = speisekarteClient.getSpeisekarte(formData);
        String websiteHtml = """
                <div class='container-fluid'>
                    <div class='row gruppenkopf'>
                        <div class='col-xs-4 gruppenname'>VORSPEISE</div>
                        <div class='col-xs-8 preise-head'>PREIS STUD.* | GÄSTE</div>
                    </div>
                    <div class='row splMeal' lang='La,Sl,GlW,V'>
                
                        <div class='col-xs-12 visible-xs-block'>
                            <span style='font-size:1.5em'>Blumenkohlsuppe</span>
                            <div style='height:10px'>&nbsp;</div>
                        </div>
                
                        <div class='col-xs-12 col-md-4 col-sm-4 contains-foto'>
                            <img src='assets/fotos/musikhochschule/Speisefotos/2-3/645ce94n5_000319.jpg?v=1' class='smallFoto img-responsive ptr' alt='Essensfoto Blumenkohlsuppe'>
                            <img src='assets/fotos/musikhochschule/Speisefotos/2-3/645ce94n5_000319.jpg?v=1' class='largeFoto hidden hidden-xs' alt='Essensfoto Blumenkohlsuppe'>
                        </div>
                        <div class='col-md-5 col-sm-5 visible-sm-block visible-md-block visible-lg-block'>
                            <span style='font-size: 16px'>Blumenkohlsuppe</span>
                            <div class='visible-sm-block visible-md-block visible-lg-block' style='margin-top:10px;'>
                                <div class='show-azn ptr' tabindex='60'>+
                                    ALLERGENE |
                                    ZUSATZSTOFFE | CO2 |
                                    NÄHRWERTE
                                </div>
                                <div class='azn hidden size-13'><br>(La, Sl, GlW)
                                    <div style='padding:10px 0'>
                                        <div style='float: left;padding-right:15px'>CO2 pro
                                            <span style='text-decoration:underline'>Portion</span><br>282 g<br><br>Nährwerte pro
                                            <span style='text-decoration:underline'>Portion</span><br>Brennwert: 889.6 kj / 212.0 kcal<br>Fett: 16.3 g<br> - davon ges. FS: 9.0 g<br>Kohlenhydrate: 11.7 g<br> - davon Zucker: 4.4 g<br>Eiweiß: 4.7 g<br>Salz: 0.6 g
                                        </div>
                                        <div style='float: left'><br><br><br>Nährwerte pro
                                            <span style='text-decoration:underline'>100 g</span><br>Brennwert: 355.8 kj / 85.0 kcal<br>Fett: 6.5 g<br> - davon ges. FS: 3.6 g<br>Kohlenhydrate: 4.7 g<br> - davon Zucker: 1.8 g<br>Eiweiß: 1.9 g<br>Salz: 0.2 g
                                        </div>
                                    </div>
                                    <div style='clear: both;height:5px'>&nbsp;</div>
                                </div>
                            </div>
                        </div>
                        <div class='col-md-3 col-sm-3 visible-sm-block visible-md-block visible-lg-block' style='text-align:right'>
                            <div> &euro;&nbsp;0,99&nbsp;/&nbsp;1,39<br></div>
                            <div style='margin-top:25px;padding-bottom:5px'><img src='assets/icons/V.png?v=1' class='iconLarge' title='vegetarisch' alt='vegetarisch'>
                            </div>
                        </div>
                
                        <div class='col-xs-6 visible-xs-block' style='height:30px;padding:0 0 10px 0'>
                            <div style='padding:0 0 20px 10px'><br><img src='assets/icons/V.png?v=1' class='icon' title='vegetarisch' alt='vegetarisch'>
                            </div>
                        </div>
                        <div class='col-xs-6 visible-xs-block' style='height:30px;text-align: right'>
                            <div style='font-size:1.1em;padding:20px 0'> &euro;&nbsp;0,99&nbsp;/&nbsp;1,39 </div>
                        </div>
                
                        <div class='col-xs-12 visible-xs-block'>
                            <div style='height:20px'>&nbsp;</div>
                            <div class='show-azn ptr' style='margin-top:10px;padding-bottom:10px' tabindex='60'>+
                                ALLERGENE |
                                ZUSATZSTOFFE | CO2 |
                                NÄHRWERTE
                            </div>
                            <div class='azn hidden size-13'>
                                <br>(La, Sl, GlW)
                                <div style='padding:10px 0'>
                                    <div style='float: left;padding-right:15px'>CO2 pro
                                        <span style='text-decoration:underline'>Portion</span><br>282 g<br><br>Nährwerte pro
                                        <span style='text-decoration:underline'>Portion</span><br>Brennwert: 889.6 kj / 212.0 kcal<br>Fett: 16.3 g<br> - davon ges. FS: 9.0 g<br>Kohlenhydrate: 11.7 g<br> - davon Zucker: 4.4 g<br>Eiweiß: 4.7 g<br>Salz: 0.6 g
                                    </div>
                                    <div style='float: left'><br><br><br>Nährwerte pro
                                        <span style='text-decoration:underline'>100 g</span><br>Brennwert: 355.8 kj / 85.0 kcal<br>Fett: 6.5 g<br> - davon ges. FS: 3.6 g<br>Kohlenhydrate: 4.7 g<br> - davon Zucker: 1.8 g<br>Eiweiß: 1.9 g<br>Salz: 0.2 g
                                    </div>
                                </div>
                                <div style='clear: both;height:5px'>&nbsp;</div>
                            </div>
                        </div>
                    </div>
                    <div class='row gruppenkopf'>
                        <div class='col-xs-4 gruppenname'>VEGANER RENNER</div>
                        <div class='col-xs-8 preise-head'>PREIS STUD.* | GÄSTE</div>
                    </div>
                    <div class='row splMeal' lang='Sw,VG,VR'>
                
                        <div class='col-xs-12 visible-xs-block'>
                            <span style='font-size:1.5em'>Kartoffel-Gemüse-Pfanne</span>
                            <div style='height:10px'>&nbsp;</div>
                        </div>
                
                        <div class='col-xs-12 col-md-4 col-sm-4 contains-foto'>
                            <img src='assets/fotos/musikhochschule/Speisefotos/8-9/mn414m7n9_000896.jpg?v=1' class='smallFoto img-responsive ptr' alt='Essensfoto Kartoffel-Gemüse-Pfanne'>
                            <img src='assets/fotos/musikhochschule/Speisefotos/8-9/mn414m7n9_000896.jpg?v=1' class='largeFoto hidden hidden-xs' alt='Essensfoto Kartoffel-Gemüse-Pfanne'>
                        </div>
                        <div class='col-md-5 col-sm-5 visible-sm-block visible-md-block visible-lg-block'>
                            <span style='font-size: 16px'>Kartoffel-Gemüse-Pfanne</span>
                            <div class='visible-sm-block visible-md-block visible-lg-block' style='margin-top:10px;'>
                                <div class='show-azn ptr' tabindex='61'>+
                                    ALLERGENE |
                                    ZUSATZSTOFFE | CO2 |
                                    NÄHRWERTE
                                </div>
                                <div class='azn hidden size-13'><br>(Sw)
                                    <div style='padding:10px 0'>
                                        <div style='float: left;padding-right:15px'>CO2 pro
                                            <span style='text-decoration:underline'>Portion</span><br>355 g<br><br>Nährwerte pro
                                            <span style='text-decoration:underline'>Portion</span><br>Brennwert: 1614.4 kj / 386.0 kcal<br>Fett: 14.9 g<br> - davon ges. FS: 1.8 g<br>Kohlenhydrate: 42.1 g<br> - davon Zucker: 13.5 g<br>Eiweiß: 13.1 g<br>Salz: 1.2 g
                                        </div>
                                        <div style='float: left'><br><br><br>Nährwerte pro
                                            <span style='text-decoration:underline'>100 g</span><br>Brennwert: 268.6 kj / 64.0 kcal<br>Fett: 2.5 g<br> - davon ges. FS: 0.3 g<br>Kohlenhydrate: 7.0 g<br> - davon Zucker: 2.3 g<br>Eiweiß: 2.2 g<br>Salz: 0.2 g
                                        </div>
                                    </div>
                                    <div style='clear: both;height:5px'>&nbsp;</div>
                                </div>
                            </div>
                        </div>
                        <div class='col-md-3 col-sm-3 visible-sm-block visible-md-block visible-lg-block' style='text-align:right'>
                            <div> &euro;&nbsp;2,99&nbsp;/&nbsp;5,49<br></div>
                            <div style='margin-top:25px;padding-bottom:5px'>
                                <img src='assets/icons/VG.png?v=1' class='iconLarge' title='vegan' alt='vegan'><img src='assets/icons/VR.png?v=1' class='iconLarge' title='Veganer Renner' alt='Veganer Renner'><img src='assets/icons/klimateller.png?v=1' class='iconLarge' title='Ein Gericht wird mit dem KlimaTeller Logo ausgezeichnet, wenn es mindestens 50% weniger CO2e Emissionen verursacht als ein vergleichbares Gericht.' alt='Ein Gericht wird mit dem KlimaTeller Logo ausgezeichnet, wenn es mindestens 50% weniger CO2e Emissionen verursacht als ein vergleichbares Gericht.'>
                            </div>
                        </div>
                
                        <div class='col-xs-6 visible-xs-block' style='height:30px;padding:0 0 10px 0'>
                            <div style='padding:0 0 20px 10px'>
                                <br><img src='assets/icons/VG.png?v=1' class='icon' title='vegan' alt='vegan'><img src='assets/icons/VR.png?v=1' class='icon' title='Veganer Renner' alt='Veganer Renner'><img src='assets/icons/klimateller.png?v=1' class='icon' title='Ein Gericht wird mit dem KlimaTeller Logo ausgezeichnet, wenn es mindestens 50% weniger CO2e Emissionen verursacht als ein vergleichbares Gericht.' alt='Ein Gericht wird mit dem KlimaTeller Logo ausgezeichnet, wenn es mindestens 50% weniger CO2e Emissionen verursacht als ein vergleichbares Gericht.'>
                            </div>
                        </div>
                        <div class='col-xs-6 visible-xs-block' style='height:30px;text-align: right'>
                            <div style='font-size:1.1em;padding:20px 0'> &euro;&nbsp;2,99&nbsp;/&nbsp;5,49 </div>
                        </div>
                
                        <div class='col-xs-12 visible-xs-block'>
                            <div style='height:20px'>&nbsp;</div>
                            <div class='show-azn ptr' style='margin-top:10px;padding-bottom:10px' tabindex='61'>+
                                ALLERGENE |
                                ZUSATZSTOFFE | CO2 |
                                NÄHRWERTE
                            </div>
                            <div class='azn hidden size-13'>
                                <br>(Sw)
                                <div style='padding:10px 0'>
                                    <div style='float: left;padding-right:15px'>CO2 pro
                                        <span style='text-decoration:underline'>Portion</span><br>355 g<br><br>Nährwerte pro
                                        <span style='text-decoration:underline'>Portion</span><br>Brennwert: 1614.4 kj / 386.0 kcal<br>Fett: 14.9 g<br> - davon ges. FS: 1.8 g<br>Kohlenhydrate: 42.1 g<br> - davon Zucker: 13.5 g<br>Eiweiß: 13.1 g<br>Salz: 1.2 g
                                    </div>
                                    <div style='float: left'><br><br><br>Nährwerte pro
                                        <span style='text-decoration:underline'>100 g</span><br>Brennwert: 268.6 kj / 64.0 kcal<br>Fett: 2.5 g<br> - davon ges. FS: 0.3 g<br>Kohlenhydrate: 7.0 g<br> - davon Zucker: 2.3 g<br>Eiweiß: 2.2 g<br>Salz: 0.2 g
                                    </div>
                                </div>
                                <div style='clear: both;height:5px'>&nbsp;</div>
                            </div>
                        </div>
                    </div>
                    <div class='row gruppenkopf'>
                        <div class='col-xs-4 gruppenname'>HAUPTGERICHT</div>
                        <div class='col-xs-8 preise-head'>PREIS STUD.* | GÄSTE</div>
                    </div>
                    <div class='row splMeal' lang='Ei,Sl,GlW,R'>
                
                        <div class='col-xs-12 visible-xs-block'>
                            <span style='font-size:1.5em'>Cevapcici mit Djuvec-Reis und Ajvar</span>
                            <div style='height:10px'>&nbsp;</div>
                        </div>
                
                        <div class='col-xs-12 col-md-4 col-sm-4 contains-foto'>
                            <img src='assets/fotos/musikhochschule/Speisefotos/2-3/000367000073000561.jpg?v=1' class='smallFoto img-responsive ptr' alt='Essensfoto Cevapcici mit Djuvec-Reis und Ajvar'>
                            <img src='assets/fotos/musikhochschule/Speisefotos/2-3/000367000073000561.jpg?v=1' class='largeFoto hidden hidden-xs' alt='Essensfoto Cevapcici mit Djuvec-Reis und Ajvar'>
                        </div>
                        <div class='col-md-5 col-sm-5 visible-sm-block visible-md-block visible-lg-block'>
                            <span style='font-size: 16px'>Cevapcici mit Djuvec-Reis und Ajvar</span>
                            <div class='visible-sm-block visible-md-block visible-lg-block' style='margin-top:10px;'>
                                <div class='show-azn ptr' tabindex='62'>+
                                    ALLERGENE |
                                    ZUSATZSTOFFE | CO2 |
                                    NÄHRWERTE
                                </div>
                                <div class='azn hidden size-13'><br>(Ei, Sl, GlW)
                                    <div style='padding:10px 0'>
                                        <div style='float: left;padding-right:15px'>CO2 pro
                                            <span style='text-decoration:underline'>Portion</span><br>1391 g<br><br>Nährwerte pro
                                            <span style='text-decoration:underline'>Portion</span><br>Brennwert: 2875.3 kj / 687.0 kcal<br>Fett: 26.5 g<br> - davon ges. FS: 10.0 g<br>Kohlenhydrate: 77.5 g<br> - davon Zucker: 14.3 g<br>Eiweiß: 29.5 g<br>Salz: 4.1 g
                                        </div>
                                        <div style='float: left'><br><br><br>Nährwerte pro
                                            <span style='text-decoration:underline'>100 g</span><br>Brennwert: 570.5 kj / 136.0 kcal<br>Fett: 5.3 g<br> - davon ges. FS: 2.0 g<br>Kohlenhydrate: 15.4 g<br> - davon Zucker: 2.8 g<br>Eiweiß: 5.9 g<br>Salz: 0.8 g
                                        </div>
                                    </div>
                                    <div style='clear: both;height:5px'>&nbsp;</div>
                                </div>
                            </div>
                        </div>
                        <div class='col-md-3 col-sm-3 visible-sm-block visible-md-block visible-lg-block' style='text-align:right'>
                            <div> &euro;&nbsp;5,00&nbsp;/&nbsp;7,50<br></div>
                            <div style='margin-top:25px;padding-bottom:5px'><img src='assets/icons/R.png?v=1' class='iconLarge' title='Rind' alt='Rind'>
                            </div>
                        </div>
                
                        <div class='col-xs-6 visible-xs-block' style='height:30px;padding:0 0 10px 0'>
                            <div style='padding:0 0 20px 10px'><br><img src='assets/icons/R.png?v=1' class='icon' title='Rind' alt='Rind'>
                            </div>
                        </div>
                        <div class='col-xs-6 visible-xs-block' style='height:30px;text-align: right'>
                            <div style='font-size:1.1em;padding:20px 0'> &euro;&nbsp;5,00&nbsp;/&nbsp;7,50 </div>
                        </div>
                
                        <div class='col-xs-12 visible-xs-block'>
                            <div style='height:20px'>&nbsp;</div>
                            <div class='show-azn ptr' style='margin-top:10px;padding-bottom:10px' tabindex='62'>+
                                ALLERGENE |
                                ZUSATZSTOFFE | CO2 |
                                NÄHRWERTE
                            </div>
                            <div class='azn hidden size-13'>
                                <br>(Ei, Sl, GlW)
                                <div style='padding:10px 0'>
                                    <div style='float: left;padding-right:15px'>CO2 pro
                                        <span style='text-decoration:underline'>Portion</span><br>1391 g<br><br>Nährwerte pro
                                        <span style='text-decoration:underline'>Portion</span><br>Brennwert: 2875.3 kj / 687.0 kcal<br>Fett: 26.5 g<br> - davon ges. FS: 10.0 g<br>Kohlenhydrate: 77.5 g<br> - davon Zucker: 14.3 g<br>Eiweiß: 29.5 g<br>Salz: 4.1 g
                                    </div>
                                    <div style='float: left'><br><br><br>Nährwerte pro
                                        <span style='text-decoration:underline'>100 g</span><br>Brennwert: 570.5 kj / 136.0 kcal<br>Fett: 5.3 g<br> - davon ges. FS: 2.0 g<br>Kohlenhydrate: 15.4 g<br> - davon Zucker: 2.8 g<br>Eiweiß: 5.9 g<br>Salz: 0.8 g
                                    </div>
                                </div>
                                <div style='clear: both;height:5px'>&nbsp;</div>
                            </div>
                        </div>
                    </div>
                    <div class='row splMeal' lang='Ei,GlW,S'>
                
                        <div class='col-xs-12 visible-xs-block'>
                            <span style='font-size:1.5em'>Paniertes Schweineschnitzel mit Bratensauce und Pommes frites</span>
                            <div style='height:10px'>&nbsp;</div>
                        </div>
                
                        <div class='col-xs-12 col-md-4 col-sm-4 contains-foto'>
                            <img src='assets/fotos/musikhochschule/Speisefotos/2-3/002231000219000017.jpg?v=1' class='smallFoto img-responsive ptr' alt='Essensfoto Paniertes Schweineschnitzel mit Bratensauce und Pommes frites'>
                            <img src='assets/fotos/musikhochschule/Speisefotos/2-3/002231000219000017.jpg?v=1' class='largeFoto hidden hidden-xs' alt='Essensfoto Paniertes Schweineschnitzel mit Bratensauce und Pommes frites'>
                        </div>
                        <div class='col-md-5 col-sm-5 visible-sm-block visible-md-block visible-lg-block'>
                            <span style='font-size: 16px'>Paniertes Schweineschnitzel mit Bratensauce und Pommes frites</span>
                            <div class='visible-sm-block visible-md-block visible-lg-block' style='margin-top:10px;'>
                                <div class='show-azn ptr' tabindex='63'>+
                                    ALLERGENE |
                                    ZUSATZSTOFFE | CO2 |
                                    NÄHRWERTE
                                </div>
                                <div class='azn hidden size-13'><br>(Ei, GlW)
                                    <div style='padding:10px 0'>
                                        <div style='float: left;padding-right:15px'>CO2 pro
                                            <span style='text-decoration:underline'>Portion</span><br>2347 g<br><br>Nährwerte pro
                                            <span style='text-decoration:underline'>Portion</span><br>Brennwert: 4003.6 kj / 956.0 kcal<br>Fett: 54.2 g<br> - davon ges. FS: 9.3 g<br>Kohlenhydrate: 78.7 g<br> - davon Zucker: 5.0 g<br>Eiweiß: 35.6 g<br>Salz: 3.7 g
                                        </div>
                                        <div style='float: left'><br><br><br>Nährwerte pro
                                            <span style='text-decoration:underline'>100 g</span><br>Brennwert: 834.1 kj / 199.0 kcal<br>Fett: 11.3 g<br> - davon ges. FS: 1.9 g<br>Kohlenhydrate: 16.4 g<br> - davon Zucker: 1.0 g<br>Eiweiß: 7.4 g<br>Salz: 0.8 g
                                        </div>
                                    </div>
                                    <div style='clear: both;height:5px'>&nbsp;</div>
                                </div>
                            </div>
                        </div>
                        <div class='col-md-3 col-sm-3 visible-sm-block visible-md-block visible-lg-block' style='text-align:right'>
                            <div> &euro;&nbsp;4,70&nbsp;/&nbsp;7,20<br></div>
                            <div style='margin-top:25px;padding-bottom:5px'><img src='assets/icons/S.png?v=1' class='iconLarge' title='Schwein' alt='Schwein'>
                            </div>
                        </div>
                
                        <div class='col-xs-6 visible-xs-block' style='height:30px;padding:0 0 10px 0'>
                            <div style='padding:0 0 20px 10px'><br><img src='assets/icons/S.png?v=1' class='icon' title='Schwein' alt='Schwein'>
                            </div>
                        </div>
                        <div class='col-xs-6 visible-xs-block' style='height:30px;text-align: right'>
                            <div style='font-size:1.1em;padding:20px 0'> &euro;&nbsp;4,70&nbsp;/&nbsp;7,20 </div>
                        </div>
                
                        <div class='col-xs-12 visible-xs-block'>
                            <div style='height:20px'>&nbsp;</div>
                            <div class='show-azn ptr' style='margin-top:10px;padding-bottom:10px' tabindex='63'>+
                                ALLERGENE |
                                ZUSATZSTOFFE | CO2 |
                                NÄHRWERTE
                            </div>
                            <div class='azn hidden size-13'>
                                <br>(Ei, GlW)
                                <div style='padding:10px 0'>
                                    <div style='float: left;padding-right:15px'>CO2 pro
                                        <span style='text-decoration:underline'>Portion</span><br>2347 g<br><br>Nährwerte pro
                                        <span style='text-decoration:underline'>Portion</span><br>Brennwert: 4003.6 kj / 956.0 kcal<br>Fett: 54.2 g<br> - davon ges. FS: 9.3 g<br>Kohlenhydrate: 78.7 g<br> - davon Zucker: 5.0 g<br>Eiweiß: 35.6 g<br>Salz: 3.7 g
                                    </div>
                                    <div style='float: left'><br><br><br>Nährwerte pro
                                        <span style='text-decoration:underline'>100 g</span><br>Brennwert: 834.1 kj / 199.0 kcal<br>Fett: 11.3 g<br> - davon ges. FS: 1.9 g<br>Kohlenhydrate: 16.4 g<br> - davon Zucker: 1.0 g<br>Eiweiß: 7.4 g<br>Salz: 0.8 g
                                    </div>
                                </div>
                                <div style='clear: both;height:5px'>&nbsp;</div>
                            </div>
                        </div>
                    </div>
                    <div class='row gruppenkopf'>
                        <div class='col-xs-4 gruppenname'>AKTION</div>
                        <div class='col-xs-8 preise-head'>PREIS STUD.* | GÄSTE</div>
                    </div>
                    <div class='row splMeal' lang='2,5,Ei,La,Sw,G'>
                
                        <div class='col-xs-12 visible-xs-block'>
                            <span style='font-size:1.5em'>Fitness Bowl mit Hähnchen und Hirtenkäse</span>
                            <div style='height:10px'>&nbsp;</div>
                        </div>
                
                        <div class='col-xs-12 col-md-4 col-sm-4 contains-foto'>
                            <div style='height:90px'>&nbsp;</div>
                        </div>
                        <div class='col-md-5 col-sm-5 visible-sm-block visible-md-block visible-lg-block'>
                            <span style='font-size: 16px'>Fitness Bowl mit Hähnchen und Hirtenkäse</span>
                            <div class='visible-sm-block visible-md-block visible-lg-block' style='margin-top:10px;'>
                                <div class='show-azn ptr' tabindex='64'>+
                                    ALLERGENE |
                                    ZUSATZSTOFFE | CO2 |
                                    NÄHRWERTE
                                </div>
                                <div class='azn hidden size-13'><br>(2, 5, Ei, La, Sw)
                                    <div style='padding:10px 0'>
                                        <div style='float: left;padding-right:15px'>CO2 pro
                                            <span style='text-decoration:underline'>Portion</span><br>1276 g<br><br>Nährwerte pro
                                            <span style='text-decoration:underline'>Portion</span><br>Brennwert: 2981.0 kj / 712.0 kcal<br>Fett: 50.7 g<br> - davon ges. FS: 13.8 g<br>Kohlenhydrate: 14.6 g<br> - davon Zucker: 12.3 g<br>Eiweiß: 49.1 g<br>Salz: 3.8 g
                                        </div>
                                        <div style='float: left'><br><br><br>Nährwerte pro
                                            <span style='text-decoration:underline'>100 g</span><br>Brennwert: 745.2 kj / 178.0 kcal<br>Fett: 12.7 g<br> - davon ges. FS: 3.5 g<br>Kohlenhydrate: 3.6 g<br> - davon Zucker: 3.1 g<br>Eiweiß: 12.3 g<br>Salz: 1.0 g
                                        </div>
                                    </div>
                                    <div style='clear: both;height:5px'>&nbsp;</div>
                                </div>
                            </div>
                        </div>
                        <div class='col-md-3 col-sm-3 visible-sm-block visible-md-block visible-lg-block' style='text-align:right'>
                            <div> &euro;&nbsp;4,75&nbsp;/&nbsp;7,25<br></div>
                            <div style='margin-top:25px;padding-bottom:5px'><img src='assets/icons/G.png?v=1' class='iconLarge' title='Geflügel' alt='Geflügel'>
                            </div>
                        </div>
                
                        <div class='col-xs-6 visible-xs-block' style='height:30px;padding:0 0 10px 0'>
                            <div style='padding:0 0 20px 10px'><br><img src='assets/icons/G.png?v=1' class='icon' title='Geflügel' alt='Geflügel'>
                            </div>
                        </div>
                        <div class='col-xs-6 visible-xs-block' style='height:30px;text-align: right'>
                            <div style='font-size:1.1em;padding:20px 0'> &euro;&nbsp;4,75&nbsp;/&nbsp;7,25 </div>
                        </div>
                
                        <div class='col-xs-12 visible-xs-block'>
                            <div style='height:20px'>&nbsp;</div>
                            <div class='show-azn ptr' style='margin-top:10px;padding-bottom:10px' tabindex='64'>+
                                ALLERGENE |
                                ZUSATZSTOFFE | CO2 |
                                NÄHRWERTE
                            </div>
                            <div class='azn hidden size-13'>
                                <br>(2, 5, Ei, La, Sw)
                                <div style='padding:10px 0'>
                                    <div style='float: left;padding-right:15px'>CO2 pro
                                        <span style='text-decoration:underline'>Portion</span><br>1276 g<br><br>Nährwerte pro
                                        <span style='text-decoration:underline'>Portion</span><br>Brennwert: 2981.0 kj / 712.0 kcal<br>Fett: 50.7 g<br> - davon ges. FS: 13.8 g<br>Kohlenhydrate: 14.6 g<br> - davon Zucker: 12.3 g<br>Eiweiß: 49.1 g<br>Salz: 3.8 g
                                    </div>
                                    <div style='float: left'><br><br><br>Nährwerte pro
                                        <span style='text-decoration:underline'>100 g</span><br>Brennwert: 745.2 kj / 178.0 kcal<br>Fett: 12.7 g<br> - davon ges. FS: 3.5 g<br>Kohlenhydrate: 3.6 g<br> - davon Zucker: 3.1 g<br>Eiweiß: 12.3 g<br>Salz: 1.0 g
                                    </div>
                                </div>
                                <div style='clear: both;height:5px'>&nbsp;</div>
                            </div>
                        </div>
                    </div>
                    <div class='row gruppenkopf'>
                        <div class='col-xs-4 gruppenname'>BEILAGE</div>
                        <div class='col-xs-8 preise-head'>PREIS STUD.* | GÄSTE</div>
                    </div>
                    <div class='row splMeal' lang='So,VG'>
                
                        <div class='col-xs-12 visible-xs-block'>
                            <span style='font-size:1.5em'>Chinagemüse</span>
                            <div style='height:10px'>&nbsp;</div>
                        </div>
                
                        <div class='col-xs-12 col-md-4 col-sm-4 contains-foto'>
                            <img src='assets/fotos/musikhochschule/Speisefotos/2-3/175msse6r_003018.jpg?v=1' class='smallFoto img-responsive ptr' alt='Essensfoto Chinagemüse'>
                            <img src='assets/fotos/musikhochschule/Speisefotos/2-3/175msse6r_003018.jpg?v=1' class='largeFoto hidden hidden-xs' alt='Essensfoto Chinagemüse'>
                        </div>
                        <div class='col-md-5 col-sm-5 visible-sm-block visible-md-block visible-lg-block'>
                            <span style='font-size: 16px'>Chinagemüse</span>
                            <div class='visible-sm-block visible-md-block visible-lg-block' style='margin-top:10px;'>
                                <div class='show-azn ptr' tabindex='65'>+
                                    ALLERGENE |
                                    ZUSATZSTOFFE | CO2 |
                                    NÄHRWERTE
                                </div>
                                <div class='azn hidden size-13'><br>(So)
                                    <div style='padding:10px 0'>
                                        <div style='float: left;padding-right:15px'>CO2 pro
                                            <span style='text-decoration:underline'>Portion</span><br>172 g<br><br>Nährwerte pro
                                            <span style='text-decoration:underline'>Portion</span><br>Brennwert: 394.1 kj / 94.0 kcal<br>Fett: 3.6 g<br> - davon ges. FS: 0.2 g<br>Kohlenhydrate: 9.7 g<br> - davon Zucker: 9.0 g<br>Eiweiß: 9.2 g<br>Salz: 1.9 g
                                        </div>
                                        <div style='float: left'><br><br><br>Nährwerte pro
                                            <span style='text-decoration:underline'>100 g</span><br>Brennwert: 246.3 kj / 59.0 kcal<br>Fett: 2.2 g<br> - davon ges. FS: 0.2 g<br>Kohlenhydrate: 6.0 g<br> - davon Zucker: 5.6 g<br>Eiweiß: 5.8 g<br>Salz: 1.2 g
                                        </div>
                                    </div>
                                    <div style='clear: both;height:5px'>&nbsp;</div>
                                </div>
                            </div>
                        </div>
                        <div class='col-md-3 col-sm-3 visible-sm-block visible-md-block visible-lg-block' style='text-align:right'>
                            <div> &euro;&nbsp;0,99&nbsp;/&nbsp;1,39<br></div>
                            <div style='margin-top:25px;padding-bottom:5px'><img src='assets/icons/VG.png?v=1' class='iconLarge' title='vegan' alt='vegan'>
                            </div>
                        </div>
                
                        <div class='col-xs-6 visible-xs-block' style='height:30px;padding:0 0 10px 0'>
                            <div style='padding:0 0 20px 10px'><br><img src='assets/icons/VG.png?v=1' class='icon' title='vegan' alt='vegan'>
                            </div>
                        </div>
                        <div class='col-xs-6 visible-xs-block' style='height:30px;text-align: right'>
                            <div style='font-size:1.1em;padding:20px 0'> &euro;&nbsp;0,99&nbsp;/&nbsp;1,39 </div>
                        </div>
                
                        <div class='col-xs-12 visible-xs-block'>
                            <div style='height:20px'>&nbsp;</div>
                            <div class='show-azn ptr' style='margin-top:10px;padding-bottom:10px' tabindex='65'>+
                                ALLERGENE |
                                ZUSATZSTOFFE | CO2 |
                                NÄHRWERTE
                            </div>
                            <div class='azn hidden size-13'>
                                <br>(So)
                                <div style='padding:10px 0'>
                                    <div style='float: left;padding-right:15px'>CO2 pro
                                        <span style='text-decoration:underline'>Portion</span><br>172 g<br><br>Nährwerte pro
                                        <span style='text-decoration:underline'>Portion</span><br>Brennwert: 394.1 kj / 94.0 kcal<br>Fett: 3.6 g<br> - davon ges. FS: 0.2 g<br>Kohlenhydrate: 9.7 g<br> - davon Zucker: 9.0 g<br>Eiweiß: 9.2 g<br>Salz: 1.9 g
                                    </div>
                                    <div style='float: left'><br><br><br>Nährwerte pro
                                        <span style='text-decoration:underline'>100 g</span><br>Brennwert: 246.3 kj / 59.0 kcal<br>Fett: 2.2 g<br> - davon ges. FS: 0.2 g<br>Kohlenhydrate: 6.0 g<br> - davon Zucker: 5.6 g<br>Eiweiß: 5.8 g<br>Salz: 1.2 g
                                    </div>
                                </div>
                                <div style='clear: both;height:5px'>&nbsp;</div>
                            </div>
                        </div>
                    </div>
                    <div class='row splMeal' lang='VG'>
                
                        <div class='col-xs-12 visible-xs-block'>
                            <span style='font-size:1.5em'>Pommes frites</span>
                            <div style='height:10px'>&nbsp;</div>
                        </div>
                
                        <div class='col-xs-12 col-md-4 col-sm-4 contains-foto'>
                            <img src='assets/fotos/musikhochschule/Speisefotos/2-3/6682e7552_002838.jpg?v=1' class='smallFoto img-responsive ptr' alt='Essensfoto Pommes frites'>
                            <img src='assets/fotos/musikhochschule/Speisefotos/2-3/6682e7552_002838.jpg?v=1' class='largeFoto hidden hidden-xs' alt='Essensfoto Pommes frites'>
                        </div>
                        <div class='col-md-5 col-sm-5 visible-sm-block visible-md-block visible-lg-block'>
                            <span style='font-size: 16px'>Pommes frites</span>
                            <div class='visible-sm-block visible-md-block visible-lg-block' style='margin-top:10px;'>
                                <div class='show-azn ptr' tabindex='66'>+
                                    ALLERGENE |
                                    ZUSATZSTOFFE | CO2 |
                                    NÄHRWERTE
                                </div>
                                <div class='azn hidden size-13'><br>&nbsp;
                                    <div style='padding:10px 0'>
                                        <div style='float: left;padding-right:15px'>CO2 pro
                                            <span style='text-decoration:underline'>Portion</span><br>297 g<br><br>Nährwerte pro
                                            <span style='text-decoration:underline'>Portion</span><br>Brennwert: 2020.4 kj / 483.0 kcal<br>Fett: 29.9 g<br> - davon ges. FS: 6.4 g<br>Kohlenhydrate: 46.2 g<br> - davon Zucker: 1.1 g<br>Eiweiß: 5.5 g<br>Salz: 1.1 g
                                        </div>
                                        <div style='float: left'><br><br><br>Nährwerte pro
                                            <span style='text-decoration:underline'>100 g</span><br>Brennwert: 918.4 kj / 219.0 kcal<br>Fett: 13.6 g<br> - davon ges. FS: 2.9 g<br>Kohlenhydrate: 21.0 g<br> - davon Zucker: 0.5 g<br>Eiweiß: 2.5 g<br>Salz: 0.5 g
                                        </div>
                                    </div>
                                    <div style='clear: both;height:5px'>&nbsp;</div>
                                </div>
                            </div>
                        </div>
                        <div class='col-md-3 col-sm-3 visible-sm-block visible-md-block visible-lg-block' style='text-align:right'>
                            <div> &euro;&nbsp;1,19&nbsp;/&nbsp;1,59<br></div>
                            <div style='margin-top:25px;padding-bottom:5px'><img src='assets/icons/VG.png?v=1' class='iconLarge' title='vegan' alt='vegan'>
                            </div>
                        </div>
                
                        <div class='col-xs-6 visible-xs-block' style='height:30px;padding:0 0 10px 0'>
                            <div style='padding:0 0 20px 10px'><br><img src='assets/icons/VG.png?v=1' class='icon' title='vegan' alt='vegan'>
                            </div>
                        </div>
                        <div class='col-xs-6 visible-xs-block' style='height:30px;text-align: right'>
                            <div style='font-size:1.1em;padding:20px 0'> &euro;&nbsp;1,19&nbsp;/&nbsp;1,59 </div>
                        </div>
                
                        <div class='col-xs-12 visible-xs-block'>
                            <div style='height:20px'>&nbsp;</div>
                            <div class='show-azn ptr' style='margin-top:10px;padding-bottom:10px' tabindex='66'>+
                                ALLERGENE |
                                ZUSATZSTOFFE | CO2 |
                                NÄHRWERTE
                            </div>
                            <div class='azn hidden size-13'>
                                <br>&nbsp;
                                <div style='padding:10px 0'>
                                    <div style='float: left;padding-right:15px'>CO2 pro
                                        <span style='text-decoration:underline'>Portion</span><br>297 g<br><br>Nährwerte pro
                                        <span style='text-decoration:underline'>Portion</span><br>Brennwert: 2020.4 kj / 483.0 kcal<br>Fett: 29.9 g<br> - davon ges. FS: 6.4 g<br>Kohlenhydrate: 46.2 g<br> - davon Zucker: 1.1 g<br>Eiweiß: 5.5 g<br>Salz: 1.1 g
                                    </div>
                                    <div style='float: left'><br><br><br>Nährwerte pro
                                        <span style='text-decoration:underline'>100 g</span><br>Brennwert: 918.4 kj / 219.0 kcal<br>Fett: 13.6 g<br> - davon ges. FS: 2.9 g<br>Kohlenhydrate: 21.0 g<br> - davon Zucker: 0.5 g<br>Eiweiß: 2.5 g<br>Salz: 0.5 g
                                    </div>
                                </div>
                                <div style='clear: both;height:5px'>&nbsp;</div>
                            </div>
                        </div>
                    </div>
                    <div class='row gruppenkopf'>
                        <div class='col-xs-4 gruppenname'>SALAT</div>
                        <div class='col-xs-8 preise-head'>PREIS STUD.* | GÄSTE</div>
                    </div>
                    <div class='row splMeal' lang='VG'>
                
                        <div class='col-xs-12 visible-xs-block'>
                            <span style='font-size:1.5em'>Beilagensalat</span>
                            <div style='height:10px'>&nbsp;</div>
                        </div>
                
                        <div class='col-xs-12 col-md-4 col-sm-4 contains-foto'>
                            <img src='assets/fotos/musikhochschule/Speisefotos/2-3/51789ss65_003211.jpg?v=1' class='smallFoto img-responsive ptr' alt='Essensfoto Beilagensalat'>
                            <img src='assets/fotos/musikhochschule/Speisefotos/2-3/51789ss65_003211.jpg?v=1' class='largeFoto hidden hidden-xs' alt='Essensfoto Beilagensalat'>
                        </div>
                        <div class='col-md-5 col-sm-5 visible-sm-block visible-md-block visible-lg-block'>
                            <span style='font-size: 16px'>Beilagensalat</span>
                            <div class='visible-sm-block visible-md-block visible-lg-block' style='margin-top:10px;'>
                                <div class='show-azn ptr' tabindex='67'>+
                                    ALLERGENE |
                                    ZUSATZSTOFFE | CO2 |
                                    NÄHRWERTE
                                </div>
                                <div class='azn hidden size-13'><br>&nbsp;
                                    <div style='padding:10px 0'>
                                        <div style='float: left;padding-right:15px'>CO2 pro
                                            <span style='text-decoration:underline'>Portion</span><br>47 g<br><br>Nährwerte pro
                                            <span style='text-decoration:underline'>Portion</span><br>Brennwert: 41.5 kj / 10.0 kcal<br>Fett: 0.1 g<br> - davon ges. FS: 0.0 g<br>Kohlenhydrate: 1.1 g<br> - davon Zucker: 0.9 g<br>Eiweiß: 0.8 g<br>Salz: 0.1 g
                                        </div>
                                        <div style='float: left'><br><br><br>Nährwerte pro
                                            <span style='text-decoration:underline'>100 g</span><br>Brennwert: 83.0 kj / 20.0 kcal<br>Fett: 0.2 g<br> - davon ges. FS: 0.1 g<br>Kohlenhydrate: 2.2 g<br> - davon Zucker: 1.8 g<br>Eiweiß: 1.6 g<br>Salz: 0.1 g
                                        </div>
                                    </div>
                                    <div style='clear: both;height:5px'>&nbsp;</div>
                                </div>
                            </div>
                        </div>
                        <div class='col-md-3 col-sm-3 visible-sm-block visible-md-block visible-lg-block' style='text-align:right'>
                            <div> &euro;&nbsp;0,99&nbsp;/&nbsp;1,39<br></div>
                            <div style='margin-top:25px;padding-bottom:5px'><img src='assets/icons/VG.png?v=1' class='iconLarge' title='vegan' alt='vegan'>
                            </div>
                        </div>
                
                        <div class='col-xs-6 visible-xs-block' style='height:30px;padding:0 0 10px 0'>
                            <div style='padding:0 0 20px 10px'><br><img src='assets/icons/VG.png?v=1' class='icon' title='vegan' alt='vegan'>
                            </div>
                        </div>
                        <div class='col-xs-6 visible-xs-block' style='height:30px;text-align: right'>
                            <div style='font-size:1.1em;padding:20px 0'> &euro;&nbsp;0,99&nbsp;/&nbsp;1,39 </div>
                        </div>
                
                        <div class='col-xs-12 visible-xs-block'>
                            <div style='height:20px'>&nbsp;</div>
                            <div class='show-azn ptr' style='margin-top:10px;padding-bottom:10px' tabindex='67'>+
                                ALLERGENE |
                                ZUSATZSTOFFE | CO2 |
                                NÄHRWERTE
                            </div>
                            <div class='azn hidden size-13'>
                                <br>&nbsp;
                                <div style='padding:10px 0'>
                                    <div style='float: left;padding-right:15px'>CO2 pro
                                        <span style='text-decoration:underline'>Portion</span><br>47 g<br><br>Nährwerte pro
                                        <span style='text-decoration:underline'>Portion</span><br>Brennwert: 41.5 kj / 10.0 kcal<br>Fett: 0.1 g<br> - davon ges. FS: 0.0 g<br>Kohlenhydrate: 1.1 g<br> - davon Zucker: 0.9 g<br>Eiweiß: 0.8 g<br>Salz: 0.1 g
                                    </div>
                                    <div style='float: left'><br><br><br>Nährwerte pro
                                        <span style='text-decoration:underline'>100 g</span><br>Brennwert: 83.0 kj / 20.0 kcal<br>Fett: 0.2 g<br> - davon ges. FS: 0.1 g<br>Kohlenhydrate: 2.2 g<br> - davon Zucker: 1.8 g<br>Eiweiß: 1.6 g<br>Salz: 0.1 g
                                    </div>
                                </div>
                                <div style='clear: both;height:5px'>&nbsp;</div>
                            </div>
                        </div>
                    </div>
                    <div class='row gruppenkopf'>
                        <div class='col-xs-4 gruppenname'>DESSERT</div>
                        <div class='col-xs-8 preise-head'>PREIS STUD.* | GÄSTE</div>
                    </div>
                    <div class='row splMeal' lang='La,V'>
                
                        <div class='col-xs-12 visible-xs-block'>
                            <span style='font-size:1.5em'>Himbeerquark</span>
                            <div style='height:10px'>&nbsp;</div>
                        </div>
                
                        <div class='col-xs-12 col-md-4 col-sm-4 contains-foto'>
                            <img src='assets/fotos/musikhochschule/Speisefotos/4-5/9cne8c535_000596.jpg?v=1' class='smallFoto img-responsive ptr' alt='Essensfoto Himbeerquark'>
                            <img src='assets/fotos/musikhochschule/Speisefotos/4-5/9cne8c535_000596.jpg?v=1' class='largeFoto hidden hidden-xs' alt='Essensfoto Himbeerquark'>
                        </div>
                        <div class='col-md-5 col-sm-5 visible-sm-block visible-md-block visible-lg-block'>
                            <span style='font-size: 16px'>Himbeerquark</span>
                            <div class='visible-sm-block visible-md-block visible-lg-block' style='margin-top:10px;'>
                                <div class='show-azn ptr' tabindex='68'>+
                                    ALLERGENE |
                                    ZUSATZSTOFFE | CO2 |
                                    NÄHRWERTE
                                </div>
                                <div class='azn hidden size-13'><br>(La)
                                    <div style='padding:10px 0'>
                                        <div style='float: left;padding-right:15px'>CO2 pro
                                            <span style='text-decoration:underline'>Portion</span><br>474 g<br><br>Nährwerte pro
                                            <span style='text-decoration:underline'>Portion</span><br>Brennwert: 668.0 kj / 160.0 kcal<br>Fett: 0.6 g<br> - davon ges. FS: 0.2 g<br>Kohlenhydrate: 18.3 g<br> - davon Zucker: 18.3 g<br>Eiweiß: 15.7 g<br>Salz: 0.2 g
                                        </div>
                                        <div style='float: left'><br><br><br>Nährwerte pro
                                            <span style='text-decoration:underline'>100 g</span><br>Brennwert: 318.1 kj / 76.0 kcal<br>Fett: 0.3 g<br> - davon ges. FS: 0.1 g<br>Kohlenhydrate: 8.7 g<br> - davon Zucker: 8.7 g<br>Eiweiß: 7.5 g<br>Salz: 0.1 g
                                        </div>
                                    </div>
                                    <div style='clear: both;height:5px'>&nbsp;</div>
                                </div>
                            </div>
                        </div>
                        <div class='col-md-3 col-sm-3 visible-sm-block visible-md-block visible-lg-block' style='text-align:right'>
                            <div> &euro;&nbsp;1,19&nbsp;/&nbsp;1,59<br></div>
                            <div style='margin-top:25px;padding-bottom:5px'><img src='assets/icons/V.png?v=1' class='iconLarge' title='vegetarisch' alt='vegetarisch'>
                            </div>
                        </div>
                
                        <div class='col-xs-6 visible-xs-block' style='height:30px;padding:0 0 10px 0'>
                            <div style='padding:0 0 20px 10px'><br><img src='assets/icons/V.png?v=1' class='icon' title='vegetarisch' alt='vegetarisch'>
                            </div>
                        </div>
                        <div class='col-xs-6 visible-xs-block' style='height:30px;text-align: right'>
                            <div style='font-size:1.1em;padding:20px 0'> &euro;&nbsp;1,19&nbsp;/&nbsp;1,59 </div>
                        </div>
                
                        <div class='col-xs-12 visible-xs-block'>
                            <div style='height:20px'>&nbsp;</div>
                            <div class='show-azn ptr' style='margin-top:10px;padding-bottom:10px' tabindex='68'>+
                                ALLERGENE |
                                ZUSATZSTOFFE | CO2 |
                                NÄHRWERTE
                            </div>
                            <div class='azn hidden size-13'>
                                <br>(La)
                                <div style='padding:10px 0'>
                                    <div style='float: left;padding-right:15px'>CO2 pro
                                        <span style='text-decoration:underline'>Portion</span><br>474 g<br><br>Nährwerte pro
                                        <span style='text-decoration:underline'>Portion</span><br>Brennwert: 668.0 kj / 160.0 kcal<br>Fett: 0.6 g<br> - davon ges. FS: 0.2 g<br>Kohlenhydrate: 18.3 g<br> - davon Zucker: 18.3 g<br>Eiweiß: 15.7 g<br>Salz: 0.2 g
                                    </div>
                                    <div style='float: left'><br><br><br>Nährwerte pro
                                        <span style='text-decoration:underline'>100 g</span><br>Brennwert: 318.1 kj / 76.0 kcal<br>Fett: 0.3 g<br> - davon ges. FS: 0.1 g<br>Kohlenhydrate: 8.7 g<br> - davon Zucker: 8.7 g<br>Eiweiß: 7.5 g<br>Salz: 0.1 g
                                    </div>
                                </div>
                                <div style='clear: both;height:5px'>&nbsp;</div>
                            </div>
                        </div>
                    </div>
                    <div class='row gruppenkopf'>
                        <div class='col-xs-4 gruppenname'>BUFFET</div>
                        <div class='col-xs-8 preise-head'>PREIS STUD.* | GÄSTE</div>
                    </div>
                    <div class='row splMeal' lang='2,3,5,8,10,Ei,La,Sl,Sf,Se,Sw,GlW,V'>
                
                        <div class='col-xs-12 visible-xs-block'>
                            <span style='font-size:1.5em'>Salatbuffet</span>
                            <div style='height:10px'>&nbsp;</div>
                        </div>
                
                        <div class='col-xs-12 col-md-4 col-sm-4 contains-foto'>
                            <div style='height:90px'>&nbsp;</div>
                        </div>
                        <div class='col-md-5 col-sm-5 visible-sm-block visible-md-block visible-lg-block'>
                            <span style='font-size: 16px'>Salatbuffet</span>
                            <div class='visible-sm-block visible-md-block visible-lg-block' style='margin-top:10px;'>
                                <div class='show-azn ptr hidden' tabindex='69'>+
                                    ALLERGENE |
                                    ZUSATZSTOFFE | CO2 |
                                    NÄHRWERTE
                                </div>
                                <div class='azn hidden size-13'><br>(2, 3, 5, 8, 10, Ei, La, Sl, Sf, Se, Sw, GlW)
                                    <div style='padding:10px 0'>
                                        <div style='float: left;padding-right:15px'>CO2 pro
                                            <span style='text-decoration:underline'>Portion</span><br>3475 g<br><br>Nährwerte pro
                                            <span style='text-decoration:underline'>Portion</span><br>Brennwert: 9999.9 kj / 999.9 kcal<br>Fett: 731.9 g<br> - davon ges. FS: 88.9 g<br>Kohlenhydrate: 376.4 g<br> - davon Zucker: 187.6 g<br>Eiweiß: 76.4 g<br>Salz: 36.5 g
                                        </div>
                                        <div style='float: left'><br><br><br>Nährwerte pro
                                            <span style='text-decoration:underline'>100 g</span><br>Brennwert: 515.6 kj / 123.0 kcal<br>Fett: 21.8 g<br> - davon ges. FS: 2.6 g<br>Kohlenhydrate: 11.1 g<br> - davon Zucker: 5.6 g<br>Eiweiß: 2.0 g<br>Salz: 1.1 g
                                        </div>
                                    </div>
                                    <div style='clear: both;height:5px'>&nbsp;</div>
                                </div>
                            </div>
                        </div>
                        <div class='col-md-3 col-sm-3 visible-sm-block visible-md-block visible-lg-block' style='text-align:right'>
                            <div> &euro;&nbsp;1,10&nbsp;/&nbsp;1,60<br>(je 100g)</div>
                            <div style='margin-top:25px;padding-bottom:5px'><img src='assets/icons/V.png?v=1' class='iconLarge' title='vegetarisch' alt='vegetarisch'>
                            </div>
                        </div>
                
                        <div class='col-xs-6 visible-xs-block' style='height:30px;padding:0 0 10px 0'>
                            <div style='padding:0 0 20px 10px'><br><img src='assets/icons/V.png?v=1' class='icon' title='vegetarisch' alt='vegetarisch'>
                            </div>
                        </div>
                        <div class='col-xs-6 visible-xs-block' style='height:30px;text-align: right'>
                            <div style='font-size:1.1em;padding:20px 0'> &euro;&nbsp;1,10&nbsp;/&nbsp;1,60 (je 100g)</div>
                        </div>
                
                        <div class='col-xs-12 visible-xs-block'>
                            <div style='height:20px'>&nbsp;</div>
                            <div class='show-azn ptr hidden' style='margin-top:10px;padding-bottom:10px' tabindex='69'>+
                                ALLERGENE |
                                ZUSATZSTOFFE | CO2 |
                                NÄHRWERTE
                            </div>
                            <div class='azn hidden size-13'>
                                <br>(2, 3, 5, 8, 10, Ei, La, Sl, Sf, Se, Sw, GlW)
                                <div style='padding:10px 0'>
                                    <div style='float: left;padding-right:15px'>CO2 pro
                                        <span style='text-decoration:underline'>Portion</span><br>3475 g<br><br>Nährwerte pro
                                        <span style='text-decoration:underline'>Portion</span><br>Brennwert: 9999.9 kj / 999.9 kcal<br>Fett: 731.9 g<br> - davon ges. FS: 88.9 g<br>Kohlenhydrate: 376.4 g<br> - davon Zucker: 187.6 g<br>Eiweiß: 76.4 g<br>Salz: 36.5 g
                                    </div>
                                    <div style='float: left'><br><br><br>Nährwerte pro
                                        <span style='text-decoration:underline'>100 g</span><br>Brennwert: 515.6 kj / 123.0 kcal<br>Fett: 21.8 g<br> - davon ges. FS: 2.6 g<br>Kohlenhydrate: 11.1 g<br> - davon Zucker: 5.6 g<br>Eiweiß: 2.0 g<br>Salz: 1.1 g
                                    </div>
                                </div>
                                <div style='clear: both;height:5px'>&nbsp;</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="container-fluid">
                    <div class="row size-13 padd-15 pdf-download visible-sm visible-md visible-lg">
                        <div class="col-sm-6">
                            Speiseplan dieser Woche <a
                                href="https://sws2.maxmanager.xyz/assets/pdf/wochenplaene/mensa-central_2024-10-14.pdf?t=1729154532"
                                class="downloadpdf" download="mensa-central_2024-10-14" tabindex="71">PDF-Download</a>
                        </div>
                        <div class="col-sm-6 text-right">
                            Speiseplan kommender Woche <a
                                href="https://sws2.maxmanager.xyz/assets/pdf/wochenplaene/mensa-central_2024-10-21.pdf?t=1729154532"
                                class="downloadpdf" download="mensa-central_2024-10-21" tabindex="72">PDF-Download</a>
                        </div>
                    </div>
                    <div class="row size-14 padd-15 pdf-download visible-xs">
                        <div class="col-xs-12">
                            Speiseplan dieser Woche <a
                                href="https://sws2.maxmanager.xyz/assets/pdf/wochenplaene/mensa-central_2024-10-14.pdf?t=1729154532"
                                class="downloadpdf" download="mensa-central_2024-10-14" tabindex="71">PDF-Download</a>
                        </div>
                        <div class="col-xs-12">
                            Speiseplan kommender Woche <a
                                href="https://sws2.maxmanager.xyz/assets/pdf/wochenplaene/mensa-central_2024-10-21.pdf?t=1729154532"
                                class="downloadpdf" download="mensa-central_2024-10-21" tabindex="72">PDF-Download</a>
                        </div>
                    </div>
                </div>
                """;

        String[] gruppen = websiteHtml.split("<div class='col-xs-4 gruppenname'>");


        return new Speisekarte(
                extractMenu(gruppen[1]),
                extractMenu(gruppen[2]),
                extractMenu(gruppen[3]),
                extractMenu(gruppen[4]),
                extractMenu(gruppen[5]),
                extractMenu(gruppen[6]),
                extractMenu(gruppen[7]));
    }
}
