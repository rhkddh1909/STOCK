package com.example.stockapi.api.stock;

import com.example.stockapi.api.util.BaseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    public void getStockInfoList_ThenCallStockInfosMethod(){
        //Stubbing (given)
        given(mockStockInfoService.stockInfos()).willReturn(List.of(new StockInfoRes(1L,"0001","주식명",100L,90L,0L,-10.0,0L)));

        //mockStockInfoController에서 getStockInfoList()를 실행할때 (when)
        BaseDto<List<StockInfoRes>> stockInfos = mockStockInfoController.getStockInfoList();

        //stockInfos()를 탔는지 검증한다.(then)
        then(mockStockInfoService).should().stockInfos();

        assertThat(stockInfos.getRsltData().get(0)).isEqualTo(new StockInfoRes(1L,"0001","주식명",100L,90L,0L,-10.0,0L));
    }
}