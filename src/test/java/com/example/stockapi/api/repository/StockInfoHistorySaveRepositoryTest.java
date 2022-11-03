package com.example.stockapi.api.repository;

import com.example.stockapi.api.repository.domain.StockInfoHistory;
import com.example.stockapi.api.stockhistory.StockInfoHistoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StockInfoHistorySaveRepositoryTest {
    @Autowired
    StockInfoHistorySaveRepository stockInfoHistorySaveRepository;

    @Test
    public void createStockInfoHistory_insertStockInfoHistory(){
        StockInfoHistory stockInfoHistory = new StockInfoHistory(1L,"000660","SK하이닉스","0001","KOSDAQ",95700L,95700L,0L,0L,0L,0.0,0L,"KOR");

        StockInfoHistory stockInfoHistory1 = new StockInfoHistory(2L,"000660","SK하이닉스","0001","KOSDAQ",95700L,95700L,0L,0L,0L,0.0,0L,"KOR");

        StockInfoHistoryDto stockInfoHistoryDto = StockInfoHistoryDto.builder()
                .stockCode("000660")
                .stockName("SK하이닉스")
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

        StockInfoHistoryDto stockInfoHistoryDto1 = StockInfoHistoryDto.builder()
                .stockCode("000660")
                .stockName("SK하이닉스")
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

        List<StockInfoHistory> list = stockInfoHistorySaveRepository.saveAll(List.of(stockInfoHistory,stockInfoHistory1));

        assertThat(list.get(0)).isEqualTo(stockInfoHistory);
        assertThat(list.get(1)).isEqualTo(stockInfoHistory1);
    }
}