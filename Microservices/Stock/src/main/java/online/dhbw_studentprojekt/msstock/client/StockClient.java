package online.dhbw_studentprojekt.msstock.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface StockClient {

    @GetExchange("/query?function=TIME_SERIES_DAILY&datatype=csv")
    String getStock(@RequestParam String symbol);

}
