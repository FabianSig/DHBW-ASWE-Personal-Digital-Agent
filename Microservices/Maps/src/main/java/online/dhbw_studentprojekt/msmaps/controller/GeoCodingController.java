package online.dhbw_studentprojekt.msmaps.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import online.dhbw_studentprojekt.dto.routing.geocoding.GeoCodingResponse;
import online.dhbw_studentprojekt.msmaps.service.GeoCodingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/geocoding")
@RequiredArgsConstructor
public class GeoCodingController {

    private final GeoCodingService geoCodingService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get the geocoding for a given address")
    public GeoCodingResponse getRouting(@RequestParam String address) {

        return geoCodingService.getGeoCoding(address);
    }

}
