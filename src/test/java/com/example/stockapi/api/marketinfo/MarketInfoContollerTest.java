package com.example.stockapi.api.marketinfo;

import com.example.stockapi.api.util.BaseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.example.stockapi.api.util.CUSTOM_CODE.RSEULT.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class MarketInfoContollerTest {
    @Mock
    MarketInfoService mockMarketInfoService;

    @InjectMocks
    MarketInfoContoller mockMarketInfoContoller;

    @Test
    public void getMarketList_thenCallGetMarketList(){
        //Stubbing (given)
        given(mockMarketInfoService.getMarketList()).willReturn(List.of(new MarketInfoRes("KOSPI","Y","KOR")));

        //getMarketList를 컨트롤러에서 호출했을때
        BaseDto<List<MarketInfoRes>> marketInfoResList = mockMarketInfoContoller.getMarketList();

        //getMarketList가 서비스에서도 호출되는지 검증
        then(mockMarketInfoService).should().getMarketList();

        assertThat(marketInfoResList.getRsltData()).isEqualTo(List.of(new MarketInfoRes("KOSPI","Y","KOR")));
    }

    @Test
    public void getMarketInfoByCode_thenCallGetMarketInfoByCode(){
        //Stubbing (given)
        given(mockMarketInfoService.getMarketInfoByCode(anyString())).willReturn(new MarketInfoRes("KOSPI","Y","KOR"));

        //getMarketList를 컨트롤러에서 호출했을때
        BaseDto<MarketInfoRes> marketInfoResList = mockMarketInfoContoller.getMarketInfoByCode("0001");

        //getMarketList가 서비스에서도 호출되는지 검증
        then(mockMarketInfoService).should().getMarketList();

        assertThat(marketInfoResList.getRsltData()).isEqualTo(new MarketInfoRes("KOSPI","Y","KOR"));
    }

    @Test
    public void getMarketInfoByNation_thenCallGetMarketInfoByNation(){
        //Stubbing (given)
        given(mockMarketInfoService.getMarketInfoByNation(anyString())).willReturn(List.of(new MarketInfoRes("KOSPI","Y","KOR")));

        //getMarketList를 컨트롤러에서 호출했을때
        BaseDto<List<MarketInfoRes>> marketInfoResList = mockMarketInfoContoller.getMarketInfoByNation("KOR");

        //getMarketList가 서비스에서도 호출되는지 검증
        then(mockMarketInfoService).should().getMarketList();

        assertThat(marketInfoResList.getRsltData()).isEqualTo(List.of(new MarketInfoRes("KOSPI","Y","KOR")));
    }

    @Test
    public void openMarket_thenCallOpenMarket(){
        //Stubbing (given)
        given(mockMarketInfoService.openMarket(anyString())).willReturn(2L);

        //openMarket을 컨트롤러에서 호출 했을 때 (when)
        BaseDto openMarketRes = mockMarketInfoContoller.openMarket("KOR");

        //openMarket을 서비스에서 호출하는지 검증 (then)
        then(mockMarketInfoService).should().openMarket(anyString());

        assertThat(openMarketRes.getStatus()).isEqualTo(SUCCESS.CODE());
    }

    @Test
    public void closeMarket_thenCallOpenMarket(){
        //Stubbing (given)
        given(mockMarketInfoService.closeMarket(anyString())).willReturn(2L);

        //openMarket을 컨트롤러에서 호출 했을 때 (when)
        BaseDto openMarketRes = mockMarketInfoContoller.closeMarket("KOR");

        //openMarket을 서비스에서 호출하는지 검증 (then)
        then(mockMarketInfoService).should().closeMarket(anyString());

        assertThat(openMarketRes.getStatus()).isEqualTo(SUCCESS.CODE());
    }
}