package com.example.stockapi.api.stock;

import com.example.stockapi.api.repository.StockInfoRepository;
import com.example.stockapi.api.repository.domain.StockInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class StockInfoServiceTest {
    @Mock
    StockInfoRepository mockStockInfoRepository;
    @InjectMocks
    StockInfoService mockStockInfoService;

    @Test
    public void stockInfos_ThenCallFindAllMethod(){
        //Stubbing Mock(given)
        given(mockStockInfoRepository.findAll()).willReturn(Optional.of(List.of(new StockInfo(1L,"0001","주식명",100L,90L,0L,0L,0L))));

        //mockStockInfoService가 StockInfos를 호출 할때 (when)
        List<StockInfoRes> stockInfoRes = mockStockInfoService.stockInfos();

        //findAll함수가 호출 되었는지 검증한다.(then)
        then(mockStockInfoRepository).should().findAll();

        assertThat(stockInfoRes.get(0)).isEqualTo(new StockInfoRes(1L,"0001","주식명",100L,90L,0L,-10.0,0L));
    }

    @Test
    public void reRanking_ThenCallfindAllMethodAndStockInfos(){
        //Stubbing Mock(given)
        given(mockStockInfoRepository.findAll()).willReturn(Optional.of(List.of(new StockInfo(1L,"0001","주식명",100L,90L,0L,0L,0L))));

        //mockStockInfoService가 reRanking 호출 할때 (when)
        Long reRankingCount = mockStockInfoService.reRanking();

        //findAll함수가 호출 되었는지 검증한다.(then)
        then(mockStockInfoRepository).should().findAll();

        assertThat(reRankingCount).isEqualTo(1L);

        List<StockInfoRes> stockInfoRes = mockStockInfoService.stockInfos();

        assertThat(stockInfoRes.get(0).getHits()).isBetween(50L,500L);
        assertThat(stockInfoRes.get(0).getTradingVolume()).isNotEqualTo(0L);
        assertThat(stockInfoRes.get(0).getCurrentPrice()).isNotEqualTo(90L);
        assertThat(stockInfoRes.get(0).getGrowthRate()).isNotEqualTo(-10.0);
    }

    @Test
    public void stockTopFiveAll_ThenCallFindTopFiveAllMethod(){
        //Stubbing (given)
        StockTopFiveAllRes<List<StockInfoRes>> stockTopFiveAllRes = new StockTopFiveAllRes<List<StockInfoRes>>();

        stockTopFiveAllRes.setStockBottomFiveGrowthRate(List.of(new StockInfoRes(),new StockInfoRes(),new StockInfoRes(),new StockInfoRes(),new StockInfoRes()));
        stockTopFiveAllRes.setStockTopFiveHits(List.of(new StockInfoRes(),new StockInfoRes(),new StockInfoRes(),new StockInfoRes(),new StockInfoRes()));
        stockTopFiveAllRes.setStockTopFiveGrowthRate(List.of(new StockInfoRes(),new StockInfoRes(),new StockInfoRes(),new StockInfoRes(),new StockInfoRes()));
        stockTopFiveAllRes.setStockTopFiveTradingVolume(List.of(new StockInfoRes(),new StockInfoRes(),new StockInfoRes(),new StockInfoRes(),new StockInfoRes()));

        given(mockStockInfoRepository.findTopFiveAll()).willReturn(Optional.ofNullable(stockTopFiveAllRes));

        //mockStockInfoService가 selectListStockTopFiveAllRes 호출 할때 (when)
        StockTopFiveAllRes<List<StockInfoRes>> testStockTopFiveAllRes = mockStockInfoService.stockTopFiveAll();

        //findTopFiveAll() 메서드가 호출되었는지 검증
        then(mockStockInfoRepository).should().findTopFiveAll();

        int objectSize = 0;
        objectSize += testStockTopFiveAllRes.getStockTopFiveHits().size();
        objectSize += testStockTopFiveAllRes.getStockTopFiveGrowthRate().size();
        objectSize += testStockTopFiveAllRes.getStockTopFiveTradingVolume().size();
        objectSize += testStockTopFiveAllRes.getStockBottomFiveGrowthRate().size();

        //4개의 분류에 5개씩 데이터 = 20
        assertThat(objectSize).isEqualTo(20);
    }
}
