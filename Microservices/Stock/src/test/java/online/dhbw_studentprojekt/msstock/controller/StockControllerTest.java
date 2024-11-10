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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

}

