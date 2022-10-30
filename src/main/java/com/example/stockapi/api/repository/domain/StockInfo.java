package com.example.stockapi.api.repository.domain;

import com.example.stockapi.api.stock.StockInfoRes;
import com.example.stockapi.api.util.Util;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.concurrent.ThreadLocalRandom;

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
    public void updateHits() {
        this.hits = hits + Util.getHitsCount(startingPrice,currentPrice);
    }

    /**
     * 현재가 업데이트
     * */
    public void updateCurrPrice(){
        Long newPrice = currentPrice + Util.getAskingPrice(currentPrice);
        Double growthRate = Util.getGrowthRate(startingPrice,newPrice);
        updateHits();
        if(Math.abs(growthRate) <= 30.0){
            updateBuyingCount();
            updateSellingCount();
            this.currentPrice = newPrice;
        }
    }

    public void updateBuyingCount() {
        this.buyingCount = buyingCount+Util.getBuyingSellingCount(startingPrice,currentPrice);
    }
    public void updateSellingCount() {
        this.sellingCount = sellingCount+Util.getBuyingSellingCount(startingPrice,currentPrice);
    }
}
