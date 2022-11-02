package com.example.stockapi.api.repository.domain;

import lombok.Getter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
@Entity
@Getter
public class StockInfoHistory{
    @EmbeddedId
    private StockInfoHistoryID stockInfoHistoryID;
    /**주식명**/
    private String stockName;
    /**시장코드**/
    private String marketCode;
    /**시장명**/
    private String marketName;
    /**시작가**/
    private Long startingPrice;
    /**종료가**/
    private Long endingPrice;
    /**매도량**/
    private Long sellingCount;
    /**매수량**/
    private Long buyingCount;
    /**거래량**/
    private Long tradingVolume;
    /**상승률**/
    private Long growthRate;

    private Long hits;

    public StockInfoHistory(StockInfoHistoryID stockInfoHistoryID, String stockName, String marketCode, String marketName, Long startingPrice, Long endingPrice, Long sellingCount, Long buyingCount, Long tradingVolume, Long growthRate, Long hits) {
        this.stockInfoHistoryID = stockInfoHistoryID;
        this.stockName = stockName;
        this.marketCode = marketCode;
        this.marketName = marketName;
        this.startingPrice = startingPrice;
        this.endingPrice = endingPrice;
        this.sellingCount = sellingCount;
        this.buyingCount = buyingCount;
        this.tradingVolume = tradingVolume;
        this.growthRate = growthRate;
        this.hits = hits;
    }

    public StockInfoHistory() {

    }
}
