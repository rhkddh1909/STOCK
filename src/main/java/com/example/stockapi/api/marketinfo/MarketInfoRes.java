package com.example.stockapi.api.marketinfo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class MarketInfoRes {
    /**시장명**/
    private String marketName;
    /**시장오픈여부**/
    private String marketOpenYn;
    /**시장국가**/
    private String marketNation;

    public MarketInfoRes(String marketName, String marketOpenYn, String marketNation) {
        this.marketName = marketName;
        this.marketOpenYn = marketOpenYn;
        this.marketNation = marketNation;
    }

    public MarketInfoRes() {

    }
}
