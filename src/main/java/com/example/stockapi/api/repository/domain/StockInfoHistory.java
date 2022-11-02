package com.example.stockapi.api.repository.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class StockInfoHistory {
    /**회차**/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sequence;
    /**종목코드**/
    @Id
    private String stockCode;
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

    public StockInfoHistory(String stockCode, String stockName, String marketCode, String marketName, Long startingPrice, Long endingPrice, Long sellingCount, Long buyingCount, Long tradingVolume, Long growthRate) {
        this.stockCode = stockCode;
        this.stockName = stockName;
        this.marketCode = marketCode;
        this.marketName = marketName;
        this.startingPrice = startingPrice;
        this.endingPrice = endingPrice;
        this.sellingCount = sellingCount;
        this.buyingCount = buyingCount;
        this.tradingVolume = tradingVolume;
        this.growthRate = growthRate;
    }

    public StockInfoHistory() {

    }
}
