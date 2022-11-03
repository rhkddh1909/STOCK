package com.example.stockapi.api.stockhistory;

import com.example.stockapi.api.util.BaseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.example.stockapi.api.util.CUSTOM_CODE.RSEULT.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class StockInfoHistoryControllerTest {
    @Mock
    StockInfoHistoryService mockStockInfoHistoryService;
    @InjectMocks
    StockInfoHistoryController mockStockInfoHistoryController;

    @Test
    public void stockInfoHistories_ThenCallStockInfoHistories(){
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

        given(mockStockInfoHistoryService.stockInfoHistories(anyString(),anyString())).willReturn(List.of(stockInfoHistoryDto1));

        //stockInfoHistories메서드를 컨트롤러에서 호출 할 때(when)
        BaseDto<List<StockInfoHistoryDto>> baseDto = mockStockInfoHistoryController.stockInfoHistories("KOR","000660");

        //stockInfoHistories메서드를 서비스에서 호출 하는지 검증(then)
        then(mockStockInfoHistoryService).should().stockInfoHistories(anyString(),anyString());

        assertThat(baseDto.getStatus()).isEqualTo(SUCCESS.CODE());
    }

    @Test
    public void stockInfoHistories_ThenCallstockInfoHistoryAnalysis(){
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

        given(mockStockInfoHistoryService.stockInfoHistoryAnalysis(anyString(),anyString(),anyLong())).willReturn(List.of(stockInfoHistoryDto1));

        //stockInfoHistories메서드를 컨트롤러에서 호출 할 때(when)
        BaseDto<List<StockInfoHistoryDto>> baseDto = mockStockInfoHistoryController.stockInfoHistoryAnalysis("KOR","000660",1L);

        //stockInfoHistories메서드를 서비스에서 호출 하는지 검증(then)
        then(mockStockInfoHistoryService).should().stockInfoHistoryAnalysis(anyString(),anyString(),anyLong());

        assertThat(baseDto.getStatus()).isEqualTo(SUCCESS.CODE());
    }
}