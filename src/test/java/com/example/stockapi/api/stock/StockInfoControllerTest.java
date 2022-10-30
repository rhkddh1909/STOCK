package com.example.stockapi.api.stock;

import com.example.stockapi.api.repository.domain.StockInfo;
import com.example.stockapi.api.util.BaseDto;
import com.example.stockapi.api.util.CUSTOM_CODE;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class StockInfoControllerTest {
    @Mock
    StockInfoService mockStockInfoService;

    @InjectMocks
    StockInfoController mockStockInfoController;

    @Test
    public void getStockInfoList_thenCallStockInfosMethod() {
        //Stubbing (given)
        given(mockStockInfoService.stockInfos()).willReturn(List.of(new StockInfoRes("0001", "주식명", 100L, 90L, 0L, -10.0, 0L)));

        //mockStockInfoController에서 getStockInfoList()를 실행할때 (when)
        BaseDto<List<StockInfoRes>> stockInfos = mockStockInfoController.getStockInfoList();

        //stockInfos()를 탔는지 검증한다.(then)
        then(mockStockInfoService).should().stockInfos();

        assertThat(stockInfos.getRsltData().get(0)).isEqualTo(new StockInfoRes("0001", "주식명", 100L, 90L, 0L, -10.0, 0L));
    }

    @Test
    public void reRanking_thenCallReRankingMethod() {
        //Stubbing (given)
        given(mockStockInfoService.reRanking()).willReturn(1L);

        //Controller에서 reRanking 함수가 호출 되었을때
        BaseDto reRanking = mockStockInfoController.reRanking();

        //Servcie애서 reRanking() 함수가 호출되었는지 검증한다.
        then(mockStockInfoService).should().reRanking();

        assertThat(reRanking.getStatus()).isEqualTo(CUSTOM_CODE.RSEULT.SUCCESS.STATUS());
    }

    @Test
    public void stockTopFiveAll_thenCallSelectStockTopFiveAll() {
        //Stubbing (given)
        given(mockStockInfoService.stockTopFiveAll()).willReturn(new StockTopFiveAllRes<List<StockInfoRes>>());

        //컨트롤러의 stockTopFiveAll 메서드를 실핼 할 때(when)
        BaseDto<StockTopFiveAllRes<List<StockInfoRes>>> testStockTopFiveAll = mockStockInfoController.stockTopFiveAll();

        //Servic에서 stockTopFiveAll() 함수가 호출되었는지 검증
        then(mockStockInfoService).should().stockTopFiveAll();

        assertThat(testStockTopFiveAll.getStatus()).isEqualTo(CUSTOM_CODE.RSEULT.SUCCESS.STATUS());
    }

    @Test
    public void stockDetailTopHits_thenCallStockDetailTopHits() {
        //Stubbing (given)
        given(mockStockInfoService.stockDetailTopHits(0, 20)).willReturn(new ArrayList<StockInfoRes>());

        //컨트롤러의 stockDetailTopHits 메서드를 실행 할 때 (when)
        BaseDto<List<StockInfoRes>> testStcokDetailTopHits = mockStockInfoController.stockDetailTopHits(0, 20);

        //Servic에서 stockTopFiveAll() 함수가 호출되었는지 검증
        then(mockStockInfoService).should().stockDetailTopHits(0,20);

        assertThat(testStcokDetailTopHits.getStatus()).isEqualTo(CUSTOM_CODE.RSEULT.SUCCESS.STATUS());
    }

    @Test
    public void stockDetailTopTradingVolume_thenCallStockDetailTopHits(){
        //Stubbing (given)
        given(mockStockInfoService.stockDetailTopTradingVolume(0,20)).willReturn(new ArrayList<StockInfoRes>());

        //컨트롤러의 stockDetailTopHits 메서드를 실행 할 때 (when)
        BaseDto<List<StockInfoRes>> testStockDetailTopTradingVolume = mockStockInfoController.stockDetailTopTradingVolume(0,20);

        //Servic에서 stockTopFiveAll() 함수가 호출되었는지 검증
        then(mockStockInfoService).should().stockDetailTopTradingVolume(0,20);

        assertThat(testStockDetailTopTradingVolume.getStatus()).isEqualTo(CUSTOM_CODE.RSEULT.SUCCESS.STATUS());
    }

    @Test
    public void stockDetailTopGrowthRate_thenCallStockDetailTopHits(){
        //Stubbing (given)
        given(mockStockInfoService.stockDetailTopGrowthRate(0,20)).willReturn(new ArrayList<StockInfoRes>());

        //컨트롤러의 stockDetailTopHits 메서드를 실행 할 때 (when)
        BaseDto<List<StockInfoRes>> testStockDetailTopGrowthRate = mockStockInfoController.stockDetailTopGrowthRate(0,20);

        //Servic에서 stockTopFiveAll() 함수가 호출되었는지 검증
        then(mockStockInfoService).should().stockDetailTopGrowthRate(0,20);

        assertThat(testStockDetailTopGrowthRate.getStatus()).isEqualTo(CUSTOM_CODE.RSEULT.SUCCESS.STATUS());
    }

    @Test
    public void stockDetailBottomGrowthRate_thenCallStockDetailTopHits(){
        //Stubbing (given)
        given(mockStockInfoService.stockDetailBottomGrowthRate(0,20)).willReturn(new ArrayList<StockInfoRes>());

        //컨트롤러의 stockDetailTopHits 메서드를 실행 할 때 (when)
        BaseDto<List<StockInfoRes>> testStockDetailBottomGrowthRate = mockStockInfoController.stockDetailBottomGrowthRate(0,20);

        //Servic에서 stockTopFiveAll() 함수가 호출되었는지 검증
        then(mockStockInfoService).should().stockDetailBottomGrowthRate(0,20);

        assertThat(testStockDetailBottomGrowthRate.getStatus()).isEqualTo(CUSTOM_CODE.RSEULT.SUCCESS.STATUS());
    }
}