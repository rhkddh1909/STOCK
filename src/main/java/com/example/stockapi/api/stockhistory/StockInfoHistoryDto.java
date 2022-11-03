package com.example.stockapi.api.stockhistory;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class StockInfoHistoryDto {
    /**종목코드**/
    private String stockCode;
    /**주식명**/
    private String stockName;

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
    private Double growthRate;
    /**조회수**/
    private Long hits;
    /**주식국가**/
    private String marketNation;

    public StockInfoHistoryDto(String stockCode, String stockName, String marketName, Long startingPrice, Long endingPrice, Long sellingCount, Long buyingCount, Long tradingVolume, Double growthRate, Long hits, String marketNation) {
        this.stockCode = stockCode;
        this.stockName = stockName;
        this.marketName = marketName;
        this.startingPrice = startingPrice;
        this.endingPrice = endingPrice;
        this.sellingCount = sellingCount;
        this.buyingCount = buyingCount;
        this.tradingVolume = tradingVolume;
        this.growthRate = growthRate;
        this.hits = hits;
        this.marketNation = marketNation;
    }

    public StockInfoHistoryDto() {
    }
}
