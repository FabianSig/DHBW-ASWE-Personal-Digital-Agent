package online.dhbw_studentprojekt.msstock.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.dhbw_studentprojekt.dto.stock.Stock;
import online.dhbw_studentprojekt.msstock.client.StockClient;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    private final StockClient stockClient;

    public Stock getStock(String symbol) {

        String stockCSV = stockClient.getStock(symbol);
        if (stockCSV.contains("Error Message")) {
            // this garbage api doesn't return a proper error code
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid symbol");
        } else if(stockCSV.contains("Thank you")) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "API rate limit exceeded");
        }
        try {
            CSVParser parser = CSVFormat.DEFAULT.builder()
                    .setHeader("timestamp", "open", "high", "low", "close", "volume")
                    .build()
                    .parse(new BufferedReader(new StringReader(stockCSV)));

            List<Stock.DataPoint> dataPoints = parser.stream().map(csvRecord -> new Stock.DataPoint(
                    csvRecord.get("timestamp"),
                    csvRecord.get("open"),
                    csvRecord.get("high"),
                    csvRecord.get("low"),
                    csvRecord.get("close"))).toList();

            return new Stock(symbol, dataPoints.get(1), dataPoints.get(2));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<Stock> getMultiple(String[] symbols) {
        return Stream.of(symbols).map(this::getStock).toList();
    }

}
