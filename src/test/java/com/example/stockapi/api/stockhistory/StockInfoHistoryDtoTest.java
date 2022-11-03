package com.example.stockapi.api.stockhistory;

import com.example.stockapi.api.repository.domain.StockInfoHistory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StockInfoHistoryDtoTest {
    @Test
    public void stockHistoryDto_getStockInfoHistory(){
        StockInfoHistory stockInfoHistory = StockInfoHistory.builder()
                .historyId(1L)
                .stockCode("000660")
                .stockName("SK하이닉스")
                .marketCode("0001")
                .marketName("KOSDAQ")
                .startingPrice(95700L)
                .endingPrice(95700L)
                .buyingCount(0L)
                .sellingCount(0L)
                .tradingVolume(0L)
                .growthRate(0.0)
                .hits(0L)
                .marketNation("KOR")
                .build();

        StockInfoHistoryDto stockInfoHistoryDto = StockInfoHistoryDto.builder()
                .historyId(1L)
                .stockCode("000660")
                .stockName("SK하이닉스")
                .marketCode("0001")
                .marketName("KOSDAQ")
                .startingPrice(95700L)
                .endingPrice(95700L)
                .buyingCount(0L)
                .sellingCount(0L)
                .tradingVolume(0L)
                .growthRate(0.0)
                .hits(0L)
                .marketNation("KOR")
                .build();

        assertThat(stockInfoHistoryDto.getStockInfoHistory()).isEqualTo(stockInfoHistory);
    }
}