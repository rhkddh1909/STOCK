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
    public void stockInfo_ThenCallFindAllMethod(){
        //Stubbing Mock(given)
        given(mockStockInfoRepository.findAll()).willReturn(Optional.of(List.of(new StockInfo(1L,"0001","주식명",100L,90L,0L,0L,0L))));

        //mockStockInfoService가 StockInfos를 호출 할때 (when)
        List<StockInfoRes> stockInfoRes = mockStockInfoService.stockInfos();

        //findAll함수가 호출 되었는지 검증한다.(then)
        then(mockStockInfoRepository).should().findAll();

        assertThat(stockInfoRes.get(0)).isEqualTo(new StockInfoRes(1L,"0001","주식명",100L,90L,0L,-10.0,0L));
    }

    @Test
    public void stockInfo_ThenCallfindAllMethodAndGhangeHits(){
        //Stubbing Mock(given)
        given(mockStockInfoRepository.findAll()).willReturn(Optional.of(List.of(new StockInfo(1L,"0001","주식명",100L,90L,0L,0L,0L))));

        //mockStockInfoService가 reRanking 호출 할때 (when)
        Long reRankingCount = mockStockInfoService.reRanking();

        //findAll함수가 호출 되었는지 검증한다.(then)
        then(mockStockInfoRepository).should().findAll();

        assertThat(reRankingCount).isEqualTo(1L);

        List<StockInfoRes> stockInfoRes = mockStockInfoService.stockInfos();

        assertThat(stockInfoRes.get(0).getHits()).isBetween(1L,3L);

    }
}