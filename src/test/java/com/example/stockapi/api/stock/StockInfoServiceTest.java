package com.example.stockapi.api.stock;

import com.example.stockapi.api.exception.MarketException;
import com.example.stockapi.api.marketinfo.MarketInfoRes;
import com.example.stockapi.api.repository.MarketInfoRepository;
import com.example.stockapi.api.repository.StockInfoRepository;
import com.example.stockapi.api.repository.domain.StockInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class StockInfoServiceTest {
    @Mock
    StockInfoRepository mockStockInfoRepository;
    @Mock
    MarketInfoRepository mockMarketInfoRepository;
    @InjectMocks
    StockInfoService mockStockInfoService;

    @Test
    public void stockInfos_ThenCallFindAllMethod(){
        //Stubbing Mock(given)
        given(mockStockInfoRepository.findByNation(anyString(),anyString())).willReturn(Optional.of(List.of(new StockInfo("0001","주식명", "시장코드", "시장명",100L,90L,0L,0L,0L,"KOR"))));

        //mockStockInfoService가 StockInfos를 호출 할때 (when)
        List<StockInfoRes> stockInfoRes = mockStockInfoService.stockInfos(anyString(),anyString());

        //findAll함수가 호출 되었는지 검증한다.(then)
        then(mockStockInfoRepository).should().findByNation(anyString(),anyString());

        assertThat(stockInfoRes.get(0)).isEqualTo(new StockInfoRes("0001","주식명", "시장명",100L,90L,0L,-10.0,0L));
    }

    @Test
    public void reRanking_ThenCallfindAllMethodAndStockInfos(){
        //Stubbing Mock(given)
        given(mockStockInfoRepository.findByNation(anyString(),anyString())).willReturn(Optional.of(List.of(new StockInfo("0001","주식명", "시장명", "시장명",100L,90L,0L,0L,0L,"KOR"))));

        //mockStockInfoService가 reRanking 호출 할때 (when)
        Long reRankingCount = mockStockInfoService.reRanking(anyString(),anyString());

        //findAll함수가 호출 되었는지 검증한다.(then)
        then(mockStockInfoRepository).should().findByNation(anyString(),anyString());

        assertThat(reRankingCount).isEqualTo(1L);

        List<StockInfoRes> stockInfoRes = mockStockInfoService.stockInfos(anyString(),anyString());

        assertThat(stockInfoRes.get(0).getHits()).isBetween(50L,500L);
        assertThat(stockInfoRes.get(0).getTradingVolume()).isNotEqualTo(0L);
        assertThat(stockInfoRes.get(0).getCurrentPrice()).isNotEqualTo(90L);
        assertThat(stockInfoRes.get(0).getGrowthRate()).isNotEqualTo(-10.0);
    }

    @Test
    public void reRanking_ThenCallfindByMarketNationAndMarketNotOpenException(){
        //Stubbing Mock(given)
        given(mockMarketInfoRepository.findByMarketNation(anyString())).willReturn(Optional.of(List.of(new MarketInfoRes("시장명", "N","KOR"),new MarketInfoRes("시장명", "Y","KOR"))));

        try {
            //mockStockInfoService가 reRanking 호출 할때 (when)
            Long reRankingCount = mockStockInfoService.reRanking("KOR","0001");
        }
        catch (MarketException e){
            //findAll함수가 호출 되었는지 검증한다.(then)
            then(mockMarketInfoRepository).should().findByMarketNation(anyString());
            assertThat(e.getMessage()).isEqualTo("시장이 열리지 않았습니다.");
        }
    }

    @Test
    public void stockTopFiveAll_ThenCallFindTopFiveAllMethod(){
        //Stubbing (given)
        StockTopFiveAllRes<List<StockInfoRes>> stockTopFiveAllRes = new StockTopFiveAllRes<List<StockInfoRes>>();

        stockTopFiveAllRes.setStockBottomFiveGrowthRate(List.of(new StockInfoRes(),new StockInfoRes(),new StockInfoRes(),new StockInfoRes(),new StockInfoRes()));
        stockTopFiveAllRes.setStockTopFiveHits(List.of(new StockInfoRes(),new StockInfoRes(),new StockInfoRes(),new StockInfoRes(),new StockInfoRes()));
        stockTopFiveAllRes.setStockTopFiveGrowthRate(List.of(new StockInfoRes(),new StockInfoRes(),new StockInfoRes(),new StockInfoRes(),new StockInfoRes()));
        stockTopFiveAllRes.setStockTopFiveTradingVolume(List.of(new StockInfoRes(),new StockInfoRes(),new StockInfoRes(),new StockInfoRes(),new StockInfoRes()));

        given(mockStockInfoRepository.findTopFiveAll(anyString(),anyString())).willReturn(stockTopFiveAllRes);

        //mockStockInfoService가 selectListStockTopFiveAllRes 호출 할때 (when)
        StockTopFiveAllRes<List<StockInfoRes>> testStockTopFiveAllRes = mockStockInfoService.stockTopFiveAll(anyString(),anyString());

        //findTopFiveAll() 메서드가 호출되었는지 검증
        then(mockStockInfoRepository).should().findTopFiveAll(anyString(),anyString());

        int objectSize = 0;
        objectSize += testStockTopFiveAllRes.getStockTopFiveHits().size();
        objectSize += testStockTopFiveAllRes.getStockTopFiveGrowthRate().size();
        objectSize += testStockTopFiveAllRes.getStockTopFiveTradingVolume().size();
        objectSize += testStockTopFiveAllRes.getStockBottomFiveGrowthRate().size();

        //4개의 분류에 5개씩 데이터 = 20
        assertThat(objectSize).isEqualTo(20);
    }

    @Test
    public void stockDetailTopHits_ThenCallFindDetailTopHitsMethod(){
        //Stubbing (given)
        Optional<List<StockInfoRes>> stockDetailTopHits = Optional.of(new ArrayList<StockInfoRes>());

        for(int i = 0; i < 20; i++){
            stockDetailTopHits.get().add(new StockInfoRes());
        }
        given(mockStockInfoRepository.findDetailTopHits(any(),anyString(),anyString())).willReturn(stockDetailTopHits);

        //service에서 stockDetailTopHits를 호출 했을 때
        List<StockInfoRes> stockTopFiveAll = mockStockInfoService.stockDetailTopHits(0,20,"KOR","0001");

        //stockInfoRepository에서 findDetailTopHits를 호출했는지 검증
        then(mockStockInfoRepository).should().findDetailTopHits(any(),anyString(),anyString());

        assertThat(stockTopFiveAll.size()).isEqualTo(20);
    }

    @Test
    public void stockDetailTopTradingVolume_ThenCallFindDetailTopTradingVolumeMethod(){
        //Stubbing (given)
        Optional<List<StockInfoRes>> stockTopFiveAllRes = Optional.of(new ArrayList<StockInfoRes>());

        for(int i = 0; i < 20; i++){
            stockTopFiveAllRes.get().add(new StockInfoRes());
        }
        given(mockStockInfoRepository.findDetailTopTradingVolume(any(),anyString(),anyString())).willReturn(stockTopFiveAllRes);

        //service에서 stockDetailTopHits를 호출 했을 때
        List<StockInfoRes> stockTopFiveAll = mockStockInfoService.stockDetailTopTradingVolume(0,20,"KOR","0001");

        //stockInfoRepository에서 findDetailTopHits를 호출했는지 검증
        then(mockStockInfoRepository).should().findDetailTopTradingVolume(any(),anyString(),anyString());

        assertThat(stockTopFiveAll.size()).isEqualTo(20);
    }

    @Test
    public void stockDetailTopGrowthRate_ThenCallFindDetailTopGrowthRateMethod(){
        //Stubbing (given)
        Optional<List<StockInfoRes>> stockTopFiveAllRes = Optional.of(new ArrayList<StockInfoRes>());

        for(int i = 0; i < 20; i++){
            stockTopFiveAllRes.get().add(new StockInfoRes());
        }
        given(mockStockInfoRepository.findDetailTopGrowthRate(any(),anyString(),anyString())).willReturn(stockTopFiveAllRes);

        //service에서 stockDetailTopHits를 호출 했을 때
        List<StockInfoRes> stockTopFiveAll = mockStockInfoService.stockDetailTopGrowthRate(0,20,"KOR","0001");

        //stockInfoRepository에서 findDetailTopHits를 호출했는지 검증
        then(mockStockInfoRepository).should().findDetailTopGrowthRate(any(),anyString(),anyString());

        assertThat(stockTopFiveAll.size()).isEqualTo(20);
    }

    @Test
    public void stockDetailTopBottomGrowthRate_ThenCallFindDetailBottomGrowthRateMethod(){
        //Stubbing (given)
        Optional<List<StockInfoRes>> stockDetailTopBottomGrowthRate = Optional.of(new ArrayList<StockInfoRes>());

        for(int i = 0; i < 20; i++){
            stockDetailTopBottomGrowthRate.get().add(new StockInfoRes());
        }

        given(mockStockInfoRepository.findDetailBottomGrowthRate(any(),anyString(),anyString())).willReturn(stockDetailTopBottomGrowthRate);

        //service에서 stockDetailTopHits를 호출 했을 때
        List<StockInfoRes> result = mockStockInfoService.stockDetailBottomGrowthRate(0,20,"KOR","0001");

        //stockInfoRepository에서 findDetailTopHits를 호출했는지 검증
        then(mockStockInfoRepository).should().findDetailBottomGrowthRate(any(),anyString(),anyString());

        assertThat(result.size()).isEqualTo(20);
    }
}
