package online.dhbw_studentprojekt.msstock.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import online.dhbw_studentprojekt.dto.stock.Stock;
import online.dhbw_studentprojekt.msstock.service.StockService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @Operation(summary = "Get a single stock")
    @GetMapping("/single")
    public Stock getSingleStock(@io.swagger.v3.oas.annotations.Parameter(example = "ALIZF") @RequestParam String symbol) {

        return stockService.getStock(symbol);
    }

    @Operation(summary = "Get multiple stocks")
    @GetMapping("/multiple")
    public List<Stock> getMultipleStock(@io.swagger.v3.oas.annotations.Parameter(example = "ALIZF,IBM") @RequestParam String[] symbol) {

        return stockService.getMultiple(symbol);
    }

}
