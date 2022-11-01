package com.example.stockapi.api.repository.domain;

import com.example.stockapi.api.stock.StockInfoRes;
import com.example.stockapi.api.util.Util;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@DynamicUpdate
public class StockInfo {
    @Id
    private String stockCode;
    private String stockName;
    private Long startingPrice;
    private Long currentPrice;
    private Long sellingCount;
    private Long buyingCount;
    private Long hits;
    protected StockInfo() {}

    public StockInfo(String stockCode, String stockName, Long startingPrice, Long currentPrice, Long sellingCount, Long buyingCount, Long hits) {
        this.stockCode = stockCode;
        this.stockName = stockName;
        this.startingPrice = startingPrice;
        this.currentPrice = currentPrice;
        this.sellingCount = sellingCount;
        this.buyingCount = buyingCount;
        this.hits = hits;
    }

    /**
     * 결과값
     * */
    public StockInfoRes getStockInfoRes() {
        return new StockInfoRes(stockCode, stockName, startingPrice, currentPrice, getTradingVolume(), Util.getGrowthRate(startingPrice,currentPrice), hits);
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
        return currentPrice + Util.getAskingPrice(currentPrice);
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
}
