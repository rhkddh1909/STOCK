package com.example.stockapi.api.repository.domain;

import com.example.stockapi.api.stock.StockInfoRes;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StockInfoTest {
    @Test
    public void stockInfo_getStockInfoResTest(){
        StockInfo stockInfoTest = new StockInfo(1L,"0001","주식명",100L,90L,10L,20L,0L);

        StockInfoRes stockInfoResTest = stockInfoTest.getStockInfoRes();

        assertThat(stockInfoResTest).isEqualTo(new StockInfoRes(1L,"0001","주식명",100L, 90L, 10L,-10.0,0L));
    }
    @Test
    public void stockInfo_getGrowthRateTest(){
        StockInfo stockInfoTest = new StockInfo(1L,"0001","주식명",100L,90L,10L,20L,0L);

        assertThat(stockInfoTest.getGrowthRate()).isEqualTo(-10.0);
    }

    @Test
    public void stockInfo_getTradingVolumeTest(){
        StockInfo stockInfoTest = new StockInfo(1L,"0001","주식명",100L,90L,10L,20L,0L);

        assertThat(stockInfoTest.getTradingVolume()).isEqualTo(10L);
    }

    @Test
    public void stockInfo_chageHitsTest(){
        StockInfo stockInfoTest = new StockInfo(1L,"0001","주식명",100L,90L,10L,20L,0L);
        stockInfoTest.changeHits();
        assertThat(stockInfoTest.getHits()).isBetween(1L,3L);
    }
}