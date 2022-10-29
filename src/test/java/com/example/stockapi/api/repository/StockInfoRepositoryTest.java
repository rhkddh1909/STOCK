package com.example.stockapi.api.repository;

import com.example.stockapi.api.repository.domain.StockInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
class StockInfoRepositoryTest {
    @Autowired
    private StockInfoRepository testStockInfoRepository;

    @Test
    public void selectStockALlInfoTest(){
        List<StockInfo> stockInfoList = testStockInfoRepository.findAll().orElse(List.of());
        assertThat(stockInfoList).isNotEmpty();
    }

    @Test
    @Transactional
    public void updateStockInfo_Hits(){
        List<StockInfo> stockInfoList = testStockInfoRepository.findAll().orElse(List.of());

        stockInfoList.stream().forEach(item->item.changeHits());

        stockInfoList = testStockInfoRepository.findAll().orElse(List.of());

        stockInfoList.stream().forEach(item->assertThat(item.getHits()).isBetween(1L,3L));

    }
}