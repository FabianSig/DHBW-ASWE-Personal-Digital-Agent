package fabiansig.client;

import online.dhbw_studentprojekt.dto.stock.Stock;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface StockClient {

    @GetExchange("/api/stock/single")
    Stock getSingleStock(@RequestParam String symbol);

    @GetExchange("/api/stock/multiple")
    List<Stock> getMultipleStock(@RequestParam List<String> symbol);
}
