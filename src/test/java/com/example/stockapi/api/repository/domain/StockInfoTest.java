package com.example.stockapi.api.repository.domain;

import com.example.stockapi.api.stock.StockInfoRes;
import com.example.stockapi.api.util.Util;
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
    public void stockInfo_getTradingVolumeTest(){
        StockInfo stockInfoTest = new StockInfo(1L,"0001","주식명",100L,90L,10L,20L,0L);

        assertThat(stockInfoTest.getTradingVolume()).isEqualTo(10L);
    }

    @Test
    public void stockInfo_updateHitsTest(){
        StockInfo stockInfoTest = new StockInfo(1L,"0001","주식명",100L,90L,10L,20L,0L);
        stockInfoTest.updateHits();
        assertThat(stockInfoTest.getHits()).isBetween(50L,500L);
    }

    @Test
    public void stockInfo_updateCurrentPriceTest(){
        StockInfo stockInfoTest = new StockInfo(1L,"0001","주식명",100L,90L,10L,20L,0L);
        for(int i = 0; i < 100; i++) {
            Long currentPrice = stockInfoTest.getCurrentPrice();
            Long hits = stockInfoTest.getHits();
            Long buyingCount = stockInfoTest.getBuyingCount();
            Long sellingCount = stockInfoTest.getSellingCount();
            Long checkCount = Util.checkGrowthRate.apply(Math.abs(Util.getGrowthRate(stockInfoTest.getStartingPrice(),stockInfoTest.getCurrentPrice())));

            stockInfoTest.updateCurrPrice();

            assertThat(Util.getGrowthRate(stockInfoTest.getStartingPrice(),stockInfoTest.getCurrentPrice())).isBetween(-30.0,30.0);
            assertThat(stockInfoTest.getHits()).isBetween(hits+(checkCount/10L),hits+checkCount);
            if(stockInfoTest.getBuyingCount() != buyingCount) {
                assertThat(stockInfoTest.getCurrentPrice()).isBetween((currentPrice + Util.checkPrice.apply(currentPrice) * -5), (currentPrice + Util.checkPrice.apply(currentPrice) * 5));
                assertThat(stockInfoTest.getBuyingCount()).isBetween(buyingCount + (checkCount / 10L), buyingCount + (checkCount * 5));
                assertThat(stockInfoTest.getSellingCount()).isBetween(sellingCount + (checkCount / 10L), sellingCount + (checkCount * 5));
            }
            else{
                assertThat(stockInfoTest.getCurrentPrice()).isEqualTo(currentPrice);
                assertThat(stockInfoTest.getBuyingCount()).isEqualTo(buyingCount);
                assertThat(stockInfoTest.getSellingCount()).isEqualTo(sellingCount);
            }
        }
    }

    @Test
    public void stockInfo_updateBuyingSellingCount(){
        StockInfo stockInfoTest = new StockInfo(1L,"0001","주식명",100L,90L,10L,20L,0L);
        stockInfoTest.updateBuyingCount();
        stockInfoTest.updateSellingCount();

        assertThat(stockInfoTest.getBuyingCount()).isBetween(50L,2500L);
        assertThat(stockInfoTest.getSellingCount()).isBetween(50L,2500L);
    }
}