package com.example.stockapi.api.repository.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Getter
@Builder
@EqualsAndHashCode
@Table(indexes =
{
    @Index(name="i_stockInfoHistoryID", columnList = "stockInfoHistoryID", unique = true)
    , @Index(name="i_marketNation", columnList = "marketNation")
})
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
    private Double growthRate;
    /**조회수**/
    private Long hits;
    /**주식국가**/
    private String marketNation;

    public StockInfoHistory(StockInfoHistoryID stockInfoHistoryID, String stockName, String marketCode, String marketName, Long startingPrice, Long endingPrice, Long sellingCount, Long buyingCount, Long tradingVolume, Double growthRate, Long hits, String marketNation) {
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
        this.marketNation = marketNation;
    }

    public StockInfoHistory() {

    }
}
