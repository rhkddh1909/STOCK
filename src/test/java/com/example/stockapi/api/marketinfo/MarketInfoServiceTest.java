package com.example.stockapi.api.marketinfo;

import com.example.stockapi.api.repository.MarketInfoRepository;
import com.example.stockapi.api.repository.StockInfoHistorySaveRepository;
import com.example.stockapi.api.repository.StockInfoRepository;
import com.example.stockapi.api.repository.domain.StockInfoHistory;
import com.example.stockapi.api.repository.domain.StockInfoHistoryID;
import com.example.stockapi.api.stockhistory.StockInfoHistoryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class MarketInfoServiceTest {
    @Mock
    MarketInfoRepository mockMarketInfoRepository;
    @Mock
    StockInfoRepository mockStockInfoRepository;
    @Mock
    StockInfoHistorySaveRepository mockStockInfoHistoryRepository;
    @InjectMocks
    MarketInfoService mockMarketInfoService;

    @Test
    public void getMarketList_thenCallFindAllMethod(){
        //Stubbing (given)
        given(mockMarketInfoRepository.findAll()).willReturn(Optional.of(List.of(new MarketInfoRes("KOSPI","Y","KOR"))));

        //getMarketList를 호출할때 (when)
        List<MarketInfoRes> marketInfoList = mockMarketInfoService.getMarketList();

        //findAll 메서드가 호출되었는지 검증
        then(mockMarketInfoRepository).should().findAll();

        assertThat(marketInfoList.get(0)).isEqualTo(new MarketInfoRes("KOSPI","Y","KOR"));
    }

    @Test
    public void getMarketInfoYnByCode_thenCallFindByMarketCode(){
        //Stubbing (given)
        given(mockMarketInfoRepository.findByMarketCode(anyString())).willReturn(Optional.of(new MarketInfoRes("KOSPI","Y","KOR")));

        //getMarketInfoByCode가 호출되었을 때(when)
        MarketInfoRes marketInfoRes = mockMarketInfoService.getMarketInfoByCode("0001");

        //레파지토리객체가 findByMarketCode를 호출하는지 검증
        then(mockMarketInfoRepository).should().findByMarketCode(anyString());

        assertThat(marketInfoRes).isEqualTo(new MarketInfoRes("KOSPI","Y","KOR"));
    }

    @Test
    public void getMarketInfoYnByNation_thenCallFindByMarketNation(){
        //Stubbing (given)
        given(mockMarketInfoRepository.findByMarketNation(anyString())).willReturn(Optional.of(List.of(new MarketInfoRes("KOSPI","Y","KOR"), new MarketInfoRes("KOSDAQ","Y","KOR"))));

        //getMarketInfoByCode가 호출되었을 때(when)
        List<MarketInfoRes> marketInfoRes = mockMarketInfoService.getMarketInfoByNation("KOR");

        //레파지토리객체가 findByMarketCode를 호출하는지 검증
        then(mockMarketInfoRepository).should().findByMarketNation(anyString());

        assertThat(marketInfoRes).isEqualTo(List.of(new MarketInfoRes("KOSPI","Y","KOR"), new MarketInfoRes("KOSDAQ","Y","KOR")));
    }

    @Test
    public void openMarket_thenCallbulkUpdateMarketInfo(){
        //Stubbing (given)
        given(mockMarketInfoRepository.bulkUpdateMarketOpen(anyString(),anyString())).willReturn(Optional.of(2L));

        //service에서 openMarket 메서드를 호출할 때(when)
        Long updateCount = mockMarketInfoService.openMarket("KOR");

        //bulkUpdateMarketOpen 메서드가 호출 되는지 검증한다. (then)
        then(mockMarketInfoRepository).should().bulkUpdateMarketOpen(anyString(),anyString());

        assertThat(updateCount).isEqualTo(2L);
    }

    @Test
    public void closeMarket_thenCallbulkUpdateMarketInfo(){
        //Stubbing (given)
        StockInfoHistory stockInfoHistory = StockInfoHistory.builder()
                .stockInfoHistoryID(StockInfoHistoryID.builder().historyId(1L).stockCode("000660").build())
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

        given(mockMarketInfoRepository.bulkUpdateMarketOpen(anyString(),anyString())).willReturn(Optional.of(2L));
        given(mockStockInfoRepository.selectStockInfoHistorys(anyString())).willReturn(Optional.of(List.of(stockInfoHistoryDto)));
        given(mockStockInfoHistoryRepository.saveAll(any())).willReturn(List.of(stockInfoHistory));

        //service에서 openMarket 메서드를 호출할 때(when)
        Long updateCount = mockMarketInfoService.closeMarket("KOR");

        //bulkUpdateMarketOpen 메서드가 호출 되는지 검증한다. (then)
        then(mockMarketInfoRepository).should().bulkUpdateMarketOpen(anyString(),anyString());
        then(mockStockInfoRepository).should().selectStockInfoHistorys(anyString());
        then(mockStockInfoHistoryRepository).should().saveAll(any());

        assertThat(updateCount).isEqualTo(2L);
    }
}