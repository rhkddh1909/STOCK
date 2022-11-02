package com.example.stockapi.api.repository.domain;

import com.example.stockapi.api.stock.StockInfoRes;
import com.example.stockapi.api.util.Util;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StockInfoTest {
    @Test
    public void stockInfo_getStockInfoResTest(){
        StockInfo stockInfoTest = new StockInfo("0001","주식명","시장코드","시장명",100L,90L,10L,20L,0L);

        StockInfoRes stockInfoResTest = stockInfoTest.getStockInfoRes();

        assertThat(stockInfoResTest).isEqualTo(new StockInfoRes("0001","주식명","시장명",100L, 90L, 10L,-10.0,0L));
    }

    @Test
    public void stockInfo_getTradingVolumeTest(){
        StockInfo stockInfoTest = new StockInfo("0001","주식명", "시장코드", "주식명",100L,90L,10L,20L,0L);

        assertThat(stockInfoTest.getTradingVolume()).isEqualTo(10L);
    }

    @Test
    public void stockInfo_updateHitsTest(){
        StockInfo stockInfoTest = new StockInfo("0001","주식명", "시장코드", "주식명",100L,90L,10L,20L,0L);
        Long tmpLong = stockInfoTest.updateHits();
        assertThat(tmpLong).isBetween(50L,500L);
    }

    @Test
    public void stockInfo_updateCurrentPriceTest(){
        StockInfo stockInfoTest = new StockInfo("0001","주식명", "시장코드", "주식명",100L,90L,10L,20L,0L);

        Long currentPrice = stockInfoTest.updateCurrPrice();

        assertThat(currentPrice).isBetween(90L+Util.checkPrice.apply(90L)*-5,90L+Util.checkPrice.apply(90L)*5);
    }

    @Test
    public void stockInfo_updateBuyingSellingCount(){
        StockInfo stockInfoTest = new StockInfo("0001","주식명", "시장코드", "주식명",100L,90L,10L,20L,0L);
        Long buyingCount = stockInfoTest.updateBuyingCount();
        Long sellingCount = stockInfoTest.updateSellingCount();

        assertThat(buyingCount).isBetween(50L,2500L);
        assertThat(sellingCount).isBetween(50L,2500L);
    }

    @Test
    public void stockInfo_reRanking(){
        StockInfo stockInfoTest = new StockInfo("0001","주식명", "시장코드", "주식명",100L,90L,10L,20L,0L);
        stockInfoTest.reRanking();

        assertThat(stockInfoTest.getStockCode()).isEqualTo(stockInfoTest.getStockCode());
        assertThat(stockInfoTest.getHits()).isBetween(10L,1000L);

        Long price = stockInfoTest.getCurrentPrice();
        Double growthRate = Math.abs(Util.getGrowthRate(100L,90L));
        assertThat(stockInfoTest.getCurrentPrice()).isBetween(price+Util.checkPrice.apply(price)*-5,price+Util.checkPrice.apply(price)*5);
        assertThat(stockInfoTest.getBuyingCount()).isBetween(0L+(Util.checkGrowthRate.apply(growthRate)/10L),0L+Util.checkGrowthRate.apply(growthRate)*5);
        assertThat(stockInfoTest.getSellingCount()).isBetween(0L+(Util.checkGrowthRate.apply(growthRate)/10L),0L+Util.checkGrowthRate.apply(growthRate)*5);
    }
}