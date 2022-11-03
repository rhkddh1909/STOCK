package com.example.stockapi.api.stockhistory;

import com.example.stockapi.api.repository.domain.StockInfoHistory;
import com.example.stockapi.api.repository.domain.StockInfoHistoryID;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class StockInfoHistoryDto {
    /**회차**/
    private Long historyId;
    /**종목코드**/
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

    public StockInfoHistoryDto(Long historyId, String stockCode, String stockName, String marketCode, String marketName, Long startingPrice, Long endingPrice, Long sellingCount, Long buyingCount, Long tradingVolume, Double growthRate, Long hits, String marketNation) {
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

    public StockInfoHistory getStockInfoHistory(){
        return StockInfoHistory.builder()
                .stockInfoHistoryID(new StockInfoHistoryID(historyId,stockCode))
                .stockName(stockName)
                .marketCode(marketCode)
                .marketName(marketName)
                .startingPrice(startingPrice)
                .endingPrice(endingPrice)
                .buyingCount(buyingCount)
                .sellingCount(sellingCount)
                .tradingVolume(tradingVolume)
                .growthRate(growthRate)
                .hits(hits)
                .marketNation(marketNation)
                .build();
    }

    public StockInfoHistoryDto() {
    }
}
