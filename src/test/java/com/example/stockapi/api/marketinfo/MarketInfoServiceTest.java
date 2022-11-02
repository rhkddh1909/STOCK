package com.example.stockapi.api.marketinfo;

import com.example.stockapi.api.repository.MarketInfoRepository;
import com.example.stockapi.api.repository.domain.MarketInfo;
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
class MarketInfoServiceTest {
    @Mock
    MarketInfoRepository mockMarketInfoRepository;

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
}