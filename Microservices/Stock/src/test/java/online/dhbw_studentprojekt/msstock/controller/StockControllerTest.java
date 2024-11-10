package online.dhbw_studentprojekt.msstock.controller;

import online.dhbw_studentprojekt.dto.stock.Stock;
import online.dhbw_studentprojekt.msstock.service.StockService;
import online.dhbw_studentprojekt.msstock.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockController.class)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class StockControllerTest {
    @MockBean
    private StockService stockService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetSingleStock_ValidSymbol() throws Exception {
        // Arrange
        String symbol = "AAPL";
        Stock mockStock = new Stock(symbol,
                new Stock.DataPoint("2024-11-07", "150.0", "155.0", "149.0", "153.0"),
                new Stock.DataPoint("2024-11-06", "145.0", "152.0", "144.0", "150.0"));
        when(stockService.getStock(symbol)).thenReturn(mockStock);

        // Act & Assert
        mockMvc.perform(get("/api/stock/single")
                        .param("symbol", symbol))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("AAPL"))
                .andExpect(jsonPath("$.yesterday.open").value("150.0"))
                .andExpect(jsonPath("$.yesterday.low").value("155.0"))
                .andExpect(jsonPath("$.yesterday.high").value("149.0"))
                .andExpect(jsonPath("$.yesterday.close").value("153.0"))

                .andExpect(jsonPath("$.yesteryesterday.open").value("145.0"))
                .andExpect(jsonPath("$.yesteryesterday.low").value("152.0"))
                .andExpect(jsonPath("$.yesteryesterday.high").value("144.0"))
                .andExpect(jsonPath("$.yesteryesterday.close").value("150.0"));
    }

    @Test
    void testGetSingleStock_InvalidSymbol() throws Exception {
        // Arrange
        String invalidSymbol = "INVALID";
        when(stockService.getStock(invalidSymbol)).thenThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid symbol"));

        // Act & Assert
        mockMvc.perform(get("/api/stock/single")
                        .param("symbol", invalidSymbol))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("404 NOT_FOUND \"Invalid symbol\"",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void testGetMultipleStock_ValidSymbols() throws Exception {
        // Arrange
        String[] symbols = {"AAPL", "GOOGL"};
        Stock stockAAPL = new Stock("AAPL",
                new Stock.DataPoint("2024-11-07", "150.0", "155.0", "149.0", "153.0"),
                new Stock.DataPoint("2024-11-06", "145.0", "152.0", "144.0", "150.0"));
        Stock stockGOOGL = new Stock("GOOGL",
                new Stock.DataPoint("2024-11-07", "2800.0", "2820.0", "2780.0", "2815.0"),
                new Stock.DataPoint("2024-11-06", "2750.0", "2800.0", "2730.0", "2770.0"));
        when(stockService.getMultiple(symbols)).thenReturn(List.of(stockAAPL, stockGOOGL));

        // Act & Assert
        mockMvc.perform(get("/api/stock/multiple")
                        .param("symbol", symbols))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("AAPL"))
                .andExpect(jsonPath("$[0].yesterday.open").value("150.0"))
                .andExpect(jsonPath("$[0].yesteryesterday.open").value("145.0"))
                .andExpect(jsonPath("$[1].name").value("GOOGL"))
                .andExpect(jsonPath("$[1].yesterday.open").value("2800.0"))
                .andExpect(jsonPath("$[1].yesteryesterday.open").value("2750.0"));
    }
    @Test
    void testGetMultipleStock_RateLimitExceeded() throws Exception {
        // Arrange
        String[] symbols = {"AAPL", "GOOGL"};
        when(stockService.getMultiple(symbols)).thenThrow(
                new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "API rate limit exceeded"));

        // Act & Assert
        mockMvc.perform(get("/api/stock/multiple")
                        .param("symbol", symbols))
                .andExpect(status().isTooManyRequests())
                .andExpect(result -> {
                    Exception resolvedException = result.getResolvedException();
                    assertNotNull(resolvedException, "Resolved exception should not be null");
                    assertTrue(resolvedException instanceof ResponseStatusException);
                    assertEquals("429 TOO_MANY_REQUESTS \"API rate limit exceeded\"", resolvedException.getMessage());
                });
    }
}

