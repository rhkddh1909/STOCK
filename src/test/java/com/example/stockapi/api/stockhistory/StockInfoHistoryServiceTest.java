package com.example.stockapi.api.stockhistory;

import com.example.stockapi.api.repository.StockInfoHistoryRepository;
import com.example.stockapi.api.repository.StockInfoHistorySaveRepository;
import com.example.stockapi.api.stock.StockInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class StockInfoHistoryServiceTest {
    @Mock
    StockInfoHistorySaveRepository mockStockInfoHistorySaveRepository;
    @Mock
    StockInfoHistoryRepository mockStockInfoHistroryRepository;
    @InjectMocks
    StockInfoHistoryService mockStockInfoHistoryService;
    @Test
    public void stockInfoHistories_ThenCallfindByNationAndStockCode(){
        //Stubbing (given)
        StockInfoHistoryDto stockInfoHistoryDto1 = StockInfoHistoryDto.builder()
                .historyId(2L)
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

        given(mockStockInfoHistroryRepository.findByNationAndStockCode(anyString(),anyString())).willReturn(Optional.of(List.of(stockInfoHistoryDto1)));

        //stockInfoHistories메소드를 서비스에서 호출 할 때 (when)
        List<StockInfoHistoryDto> stockInfoHistoryDtos = mockStockInfoHistoryService.stockInfoHistories("KOR","000660");

        //findByNationAndStockCode 메서드가 호출되는지 검증
        then(mockStockInfoHistroryRepository).should().findByNationAndStockCode(anyString(),anyString());

        assertThat(stockInfoHistoryDtos).hasSize(1);
        assertThat(stockInfoHistoryDtos.get(0)).isEqualTo(stockInfoHistoryDto1);
    }
}