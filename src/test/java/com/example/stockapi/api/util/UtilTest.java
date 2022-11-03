package com.example.stockapi.api.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
class UtilTest {
    @Test
    public void util_getErrorBodyTest(){
        String message = "test";
        BaseDto errorDto = Util.getErrorBody(message, new Object());

        assertThat(errorDto.getStatus()).isEqualTo(CUSTOM_CODE.RSEULT.ERROR.STATUS());
        assertThat(errorDto.getRsltMsg()).isEqualTo(message);
    }

    @Test
    public void util_getAskingPriceTest(){
        Long currentPrice = 5000000L;
        Long buyingCount = 2000L;
        Long sellingCount = 1000L;
        Long askingPrice = Util.getAskingPrice(currentPrice,buyingCount,sellingCount);

        assertThat(askingPrice).isIn(List.of(1L,5L,10L,50L,100L,500L,1000L));
    }

    @Test
    public void util_getGrowthRateTest(){
        Long startingPrice = 100L;
        Long currentPrice = 90L;

        Double growthRate = Util.getGrowthRate(startingPrice,currentPrice);

        Double dStartingPrice = Double.valueOf(startingPrice);
        Double dcurrentPrice = Double.valueOf(currentPrice);

        assertThat(growthRate).isEqualTo(Double.valueOf(String.format("%.2f",((dcurrentPrice - dStartingPrice)/dStartingPrice) * 100.0)));
    }

    @Test
    public void getHitsCountTest(){
        Long startingPrice = 129L;
        Long currentPrice = 100L;
        Long hitsCount = Util.getHitsCount(startingPrice, currentPrice);

        assertThat(hitsCount).isBetween(100L,1000L);
    }

    @Test
    public void util_getBuyingSellingCountTest(){
        Long startingPrice = 129L;
        Long currentPrice = 100L;

        Long buyingSellingCount = Util.getBuyingSellingCount(startingPrice, currentPrice);

        assertThat(buyingSellingCount).isBetween(100L,5000L);
    }
}