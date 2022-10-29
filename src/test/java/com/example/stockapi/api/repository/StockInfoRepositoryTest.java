package com.example.stockapi.api.repository;

import com.example.stockapi.api.repository.domain.StockInfo;
import com.example.stockapi.api.util.Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.IntStream;

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
    public void updateStockInfo_HitsTest(){
        List<StockInfo> stockInfoList = testStockInfoRepository.findAll().orElse(List.of());

        stockInfoList.stream().forEach(item->item.updateHits());

        stockInfoList = testStockInfoRepository.findAll().orElse(List.of());

        stockInfoList.stream().forEach(item->assertThat(item.getHits()).isBetween(1L,3L));

    }

    @Test
    @Transactional
    public void updateStockInfo_currentPriceTest(){
        List<StockInfo> stockInfoList = testStockInfoRepository.findAll().orElse(List.of());

        List<Long> askingPriceList = stockInfoList.stream()
                .map(item-> Util.checkPrice.apply(item.getCurrentPrice()))
                .toList();
        List<Long> checkGrowthRateList = IntStream.range(0,stockInfoList.size())
                .mapToLong(item -> Util.checkGrowthRate.apply(Util.getGrowthRate(stockInfoList.get(item).getStartingPrice(),stockInfoList.get(item).getCurrentPrice())))
                .boxed()
                .toList();

        stockInfoList.stream().forEach(item->item.updateCurrPrice());

        List<StockInfo> stockInfoAfterList = testStockInfoRepository.findAll().orElse(List.of());

        IntStream.range(0,stockInfoList.size()).forEach(item->{
            assertThat(stockInfoAfterList.get(item).getCurrentPrice())
                    .isBetween(stockInfoAfterList.get(item).getStartingPrice()+(askingPriceList.get(item)*-5)
                            ,stockInfoAfterList.get(item).getStartingPrice()+(askingPriceList.get(item)*5));
            assertThat(stockInfoAfterList.get(item).getHits()).isBetween(checkGrowthRateList.get(item)/10,checkGrowthRateList.get(item));
            assertThat(stockInfoAfterList.get(item).getBuyingCount()).isBetween(checkGrowthRateList.get(item)/10,checkGrowthRateList.get(item)*5);
            assertThat(stockInfoAfterList.get(item).getSellingCount()).isBetween(checkGrowthRateList.get(item)/10,checkGrowthRateList.get(item)*5);
        });

    }
}