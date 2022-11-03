package com.example.stockapi.api.stock;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class StockInfoRes {
   /**종목코드**/
    private String stockCode;
    /**종목명**/
    private String stockName;
    /**시장명**/
    private String marketName;
    /**시작가**/
    private Long startingPrice;
    /**현재가**/
    private Long currentPrice;
    /**거래량**/
    private Long tradingVolume;
    /**상승률**/
    private Double growthRate;
    /**조회수**/
    private Long hits;

    public StockInfoRes(String stockCode, String stockName, String marketName, Long startingPrice, Long currentPrice, Long tradingVolume, Double growthRate, Long hits) {
        this.stockCode = stockCode;
        this.stockName = stockName;
        this.marketName = marketName;
        this.startingPrice = startingPrice;
        this.currentPrice = currentPrice;
        this.tradingVolume = tradingVolume;
        this.growthRate = growthRate;
        this.hits = hits;
    }

    public StockInfoRes() {}

}
