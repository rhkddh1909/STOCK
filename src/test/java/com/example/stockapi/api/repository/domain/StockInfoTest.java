package com.example.stockapi.api.repository.domain;

import com.example.stockapi.api.stock.StockInfoRes;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StockInfoTest {
    @Test
    public void getStockInfoTest(){
        StockInfo stockInfoTest = new StockInfo(1L,"0001","주식명",100L,90L,10L,20L,0L);

        StockInfoRes stockInfoResTest = stockInfoTest.getStockInfoRes();

        Double growthRate = stockInfoTest.getGrowthRate();

        assertThat(growthRate).isEqualTo(-10.0);

        assertThat(stockInfoResTest).isEqualTo(new StockInfoRes(1L,"0001","주식명",100L, 90L, 10L,-10.0,0L));

        assertThat(stockInfoResTest.getTradingVolume()).isEqualTo(10L);
    }
}