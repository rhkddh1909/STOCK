package com.example.stockapi.api.repository.domain;

import com.example.stockapi.api.stock.StockInfoRes;
import com.example.stockapi.api.util.Util;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Getter
@DynamicUpdate
@Table(indexes =
{
    @Index(name="i_stockCode", columnList = "stockCode")
    , @Index(name="i_marketCode", columnList = "marketCode")
    , @Index(name="i_marketNation", columnList = "marketNation")
})
public class StockInfo {
    /**종목 코드**/
    @Id
    private String stockCode;
    /**주식명**/
    private String stockName;
    /**시장 코드**/
    private String marketCode;
    /**시장명**/
    private String marketName;
    /**시작가**/
    private Long startingPrice;
    /**현재가**/
    private Long currentPrice;
    /**매도량**/
    private Long sellingCount;
    /**매수량**/
    private Long buyingCount;
    /**조회수**/
    private Long hits;
    /**주식국가**/
    private String marketNation;

    protected StockInfo() {}

    public StockInfo(String stockCode, String stockName, String marketCode, String marketName, Long startingPrice, Long currentPrice, Long sellingCount, Long buyingCount, Long hits, String marketNation) {
        this.stockCode = stockCode;
        this.stockName = stockName;
        this.marketCode = marketCode;
        this.marketName = marketName;
        this.startingPrice = startingPrice;
        this.currentPrice = currentPrice;
        this.sellingCount = sellingCount;
        this.buyingCount = buyingCount;
        this.hits = hits;
        this.marketNation = marketNation;
    }

    /**
     * 결과값
     * */
    public StockInfoRes getStockInfoRes() {
        return new StockInfoRes(stockCode, stockName, marketName, startingPrice, currentPrice, getTradingVolume(), Util.getGrowthRate(startingPrice,currentPrice), hits);
    }

    /**
     * 거래량
     * */
    public Long getTradingVolume() {
        return Math.min(sellingCount,buyingCount);
    }

    /**
     * 조회수 업데이트
     * */
    public long updateHits() {
        return hits + Util.getHitsCount(startingPrice,currentPrice);
    }

    /**
     * 현재가 업데이트
     * */
    public Long updateCurrPrice(){
        return currentPrice + Util.getAskingPrice(currentPrice,buyingCount,sellingCount);
    }

    /**
     * 매수 업데이트
     * */
    public Long updateBuyingCount() {
        return buyingCount+Util.getBuyingSellingCount(startingPrice,currentPrice);
    }

    /**
     * 매도 업데이트
     * */
    public Long updateSellingCount() {
        return sellingCount+Util.getBuyingSellingCount(startingPrice,currentPrice);
    }

    public Long reRanking() {
        Long newPrice = updateCurrPrice();
        Double growthRate = Math.abs(Util.getGrowthRate(startingPrice,newPrice));
        this.hits = updateHits();
        this.currentPrice = growthRate <= 30.0 ? newPrice : currentPrice;
        this.buyingCount = growthRate <= 30.0 ? updateBuyingCount() : buyingCount;
        this.sellingCount = growthRate <= 30.0 ? updateSellingCount() : sellingCount;

        return 1L;
    }

    public void stockInfoInit(){
        this.sellingCount = 0L;
        this.buyingCount = 0L;
        this.hits = 0L;
        this.startingPrice = currentPrice;
    }
}
