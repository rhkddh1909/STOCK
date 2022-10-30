package com.example.stockapi.api.stock;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class StockInfoRes {
    private String stockCode;

    private String stockName;

    private Long startingPrice;

    private Long currentPrice;

    private Long tradingVolume;
    private Double growthRate;
    private Long hits;

    public StockInfoRes(String stockCode, String stockName, Long startingPrice, Long currentPrice, Long tradingVolume, Double growthRate, Long hits) {
        this.stockCode = stockCode;
        this.stockName = stockName;
        this.currentPrice = currentPrice;
        this.startingPrice = startingPrice;
        this.tradingVolume = tradingVolume;
        this.growthRate = growthRate;
        this.hits = hits;
    }

    public StockInfoRes() {}

}
