package com.example.stockapi.api.repository.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MarketInfoTest {
    @Test
    public void markeInfo_updateMarketOpenYn(){
        MarketInfo marketInfo = new MarketInfo("0001","코스피","N", "KOR");

        marketInfo.updateMarketOpenYn("Y");

        assertThat(marketInfo.getMarketOpenYn()).isEqualTo("Y");
    }
}