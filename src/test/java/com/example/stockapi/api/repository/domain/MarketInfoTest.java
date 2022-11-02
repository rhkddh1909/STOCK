package com.example.stockapi.api.repository.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class MarketInfoTest {
    @Test
    public void markeInfo_updateMarketOpenYn(){
        MarketInfo marketInfo = new MarketInfo("0001","코스피","N", "KOR",0L);

        marketInfo.updateMarketOpenYn("Y");

        assertThat(marketInfo.getMarketOpenYn()).isEqualTo("Y");
        assertThat(marketInfo.getMarketSequence()).isEqualTo(1L);
    }
}