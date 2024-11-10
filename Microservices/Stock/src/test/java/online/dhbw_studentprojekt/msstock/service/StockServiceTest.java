package online.dhbw_studentprojekt.msstock.service;

import online.dhbw_studentprojekt.dto.stock.Stock;
import online.dhbw_studentprojekt.msstock.client.StockClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {

    @Mock
    private StockClient stockClient;

    @InjectMocks
    private StockService stockService;

    @Test
    void testGetStock_ValidSymbol() {
        // Arrange
        String validSymbol = "AAPL";
        String csvData = "timestamp,open,high,low,close,volume\n" +
                "2024-11-08,227.1700,228.6600,226.4050,226.9600,38328824\n" +
                "2024-11-07,224.6250,227.8750,224.5700,227.4800,42137691\n";
        when(stockClient.getStock(validSymbol)).thenReturn(csvData);

        // Act
        Stock stock = stockService.getStock(validSymbol);

        // Assert
        assertNotNull(stock);
        assertEquals(validSymbol, stock.name());
        assertEquals("227.1700", stock.yesterday().open());
        assertEquals("226.9600", stock.yesterday().close());

        assertEquals("224.6250", stock.yesteryesterday().open());
        assertEquals("227.4800", stock.yesteryesterday().close());
        verify(stockClient, times(1)).getStock(validSymbol);
    }

    @Test
    void testGetStock_InvalidSymbol() {
        // Arrange
        String invalidSymbol = "INVALID";
        String errorResponse = "{\n" +
                "    \"Error Message\": \"Invalid API call. Please retry or visit the documentation (https://www.alphavantage.co/documentation/) for TIME_SERIES_DAILY.\"\n" +
                "}";
        when(stockClient.getStock(invalidSymbol)).thenReturn(errorResponse);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> stockService.getStock(invalidSymbol),
                "Expected ResponseStatusException for invalid symbol");
        assertEquals("404 NOT_FOUND \"Invalid symbol\"", exception.getMessage());
        verify(stockClient, times(1)).getStock(invalidSymbol);
    }

    @Test
    void testGetStock_RateLimitExceeded() {
        // Arrange
        String symbol = "AAPL";
        String rateLimitResponse = "{\n" +
                "    \"Information\": \"Thank you for using Alpha Vantage! Our standard API rate limit is 25 requests per day. Please subscribe to any of the premium plans at https://www.alphavantage.co/premium/ to instantly remove all daily rate limits.\"\n" +
                "}";
        when(stockClient.getStock(symbol)).thenReturn(rateLimitResponse);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> stockService.getStock(symbol),
                "Expected ResponseStatusException for API rate limit exceeded");
        assertEquals("429 TOO_MANY_REQUESTS \"API rate limit exceeded\"", exception.getMessage());
        verify(stockClient, times(1)).getStock(symbol);
    }

    @Test
    void testGetMultipleStocks_ValidSymbols() {
        // Arrange
        String[] symbols = {"AAPL", "GOOGL"};
        String csvData = "timestamp,open,high,low,close,volume\n" +
                "2024-11-07,150.0,155.0,149.0,153.0,100000\n" +
                "2024-11-06,145.0,152.0,144.0,150.0,120000\n";
        when(stockClient.getStock("AAPL")).thenReturn(csvData);
        when(stockClient.getStock("GOOGL")).thenReturn(csvData);

        // Act
        List<Stock> stocks = stockService.getMultiple(symbols);

        // Assert
        assertNotNull(stocks);
        assertEquals(2, stocks.size());
        assertEquals("AAPL", stocks.get(0).name());
        assertEquals("GOOGL", stocks.get(1).name());
        verify(stockClient, times(1)).getStock("AAPL");
        verify(stockClient, times(1)).getStock("GOOGL");
    }

    /* Implement a way to check for proper data then add test
    @Test
    void testGetStock_MalformedCSV() {
        // Arrange
        String symbol = "AAPL";
        String malformedCsv = "timestamp,open,high,low,close,volume\n" +
                "valid,response,without,proper,data\n" +
                "valid,response,without,proper,data\n";
        when(stockClient.getStock(symbol)).thenReturn(malformedCsv);

        System.out.println(stockService.getStock(symbol).yesterday().open()); -> Would print out "response"

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> stockService.getStock(symbol),
                "Expected ResponseStatusException for malformed CSV data");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        verify(stockClient, times(1)).getStock(symbol);
    }
     */
}

