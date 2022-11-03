package com.example.stockapi.api.repository.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@EqualsAndHashCode
@IdClass(StockInfoHistoryId.class)
@Table(indexes =
{
    @Index(name="i_historyId", columnList = "historyId")
    , @Index(name="i_stockCode2", columnList = "stockCode")
    , @Index(name="i_marketNation2", columnList = "marketNation")
})
public class StockInfoHistory{
    @Id
    private Long historyId;
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
    private Double growthRate;
    /**조회수**/
    private Long hits;
    /**주식국가**/
    private String marketNation;

    public StockInfoHistory(Long historyId, String stockCode, String stockName, String marketCode, String marketName, Long startingPrice, Long endingPrice, Long sellingCount, Long buyingCount, Long tradingVolume, Double growthRate, Long hits, String marketNation) {
        this.historyId = historyId;
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
        this.hits = hits;
        this.marketNation = marketNation;
    }

    public StockInfoHistory() {

    }
}
