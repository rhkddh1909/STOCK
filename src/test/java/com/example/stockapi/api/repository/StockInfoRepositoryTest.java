package com.example.stockapi.api.repository;

import com.example.stockapi.api.repository.domain.StockInfo;
import com.example.stockapi.api.stock.StockInfoRes;
import com.example.stockapi.api.stock.StockTopFiveAllRes;
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
    public void selectStockInfo_findAllTest(){
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

    @Test
    public void selectStockInfo_findHitsTopFiveTest(){
        List<StockInfo> stockInfoList = testStockInfoRepository.findHitsTopFive().orElse(List.of());

        assertThat(stockInfoList).isNotEmpty();
        assertThat(stockInfoList.size()).isEqualTo(5);
    }

    @Test
    public void selectStockInfo_findTradingVolumeTopFiveTest(){
        List<StockInfoRes> stockInfoResList = testStockInfoRepository.findTradingVolumeTopFive().orElse(List.of());

        assertThat(stockInfoResList).isNotEmpty();
        assertThat(stockInfoResList.size()).isEqualTo(5);
    }

    @Test
    public void selectStockInfo_findGrowthRateTopFiveTest(){
        List<StockInfoRes> stockInfoResList = testStockInfoRepository.findGrowthRateTopFive().orElse(List.of());

        assertThat(stockInfoResList).isNotEmpty();
        assertThat(stockInfoResList.size()).isEqualTo(5);
    }

    @Test
    public void selectStockInfo_findGrowthRateBottomFiveTest(){
        List<StockInfoRes> stockInfoResList = testStockInfoRepository.findGrowthRateBottomFive().orElse(List.of());

        assertThat(stockInfoResList).isNotEmpty();
        assertThat(stockInfoResList.size()).isEqualTo(5);
    }

    @Test
    public void selectStockInfo_findTopFiveAllTest(){
        StockTopFiveAllRes<List<StockInfoRes>> stockTopFiveAllRes = testStockInfoRepository.findTopFiveAll().orElse(new StockTopFiveAllRes<List<StockInfoRes>>());

        assertThat(stockTopFiveAllRes.getStockTopFiveHits()).isNotEmpty();
        assertThat(stockTopFiveAllRes.getStockTopFiveHits().size()).isEqualTo(5);

        assertThat(stockTopFiveAllRes.getStockTopFiveTradingVolume()).isNotEmpty();
        assertThat(stockTopFiveAllRes.getStockTopFiveTradingVolume().size()).isEqualTo(5);

        assertThat(stockTopFiveAllRes.getStockTopFiveGrowthRate()).isNotEmpty();
        assertThat(stockTopFiveAllRes.getStockTopFiveGrowthRate().size()).isEqualTo(5);

        assertThat(stockTopFiveAllRes.getStockBottomFiveGrowthRate()).isNotEmpty();
        assertThat(stockTopFiveAllRes.getStockBottomFiveGrowthRate().size()).isEqualTo(5);
    }
}