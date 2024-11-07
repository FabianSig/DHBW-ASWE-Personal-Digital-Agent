package online.dhbw_studentprojekt.msmaps.controller;

import online.dhbw_studentprojekt.dto.routing.geocoding.GeoCodingResponse;
import online.dhbw_studentprojekt.msmaps.service.GeoCodingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/geocoding")
@RequiredArgsConstructor
public class GeoCodingController {

    private final GeoCodingService geoCodingService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public GeoCodingResponse getRouting(@RequestParam String address) {
        return geoCodingService.getGeoCoding(address);
    }
}
