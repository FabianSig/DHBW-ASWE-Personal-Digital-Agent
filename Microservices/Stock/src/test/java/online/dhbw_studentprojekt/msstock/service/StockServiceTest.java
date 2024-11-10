package online.dhbw_studentprojekt.msstock.service;

import online.dhbw_studentprojekt.dto.stock.Stock;
import online.dhbw_studentprojekt.msstock.client.StockClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
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

}

