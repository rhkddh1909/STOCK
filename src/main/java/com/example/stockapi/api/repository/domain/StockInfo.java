package com.example.stockapi.api.repository.domain;

import com.example.stockapi.api.stock.StockInfoRes;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String stockCode;
    private String stockName;
    private Long startingPrice;
    private Long currentPrice;
    private Long sellingCount;
    private Long buyingCount;
    private Long hits;
    protected StockInfo() {}

    public StockInfo(Long id, String stockCode, String stockName, Long startingPrice, Long currentPrice, Long sellingCount, Long buyingCount, Long hits) {
        this.id = id;
        this.stockCode = stockCode;
        this.stockName = stockName;
        this.startingPrice = startingPrice;
        this.currentPrice = currentPrice;
        this.sellingCount = sellingCount;
        this.buyingCount = buyingCount;
        this.hits = hits;
    }

    public StockInfoRes getStockInfoRes() {
        return new StockInfoRes(id, stockCode, stockName, startingPrice, currentPrice, getTradingVolume(), getGrowthRate(), hits);
    }

    public Double getGrowthRate() {
        Double tmpStartingPrice = Double.valueOf(startingPrice);
        Double tmpCurrentPrice = Double.valueOf(currentPrice);

        return Double.valueOf(String.format("%.2f",((tmpCurrentPrice - tmpStartingPrice)/tmpStartingPrice) * 100.0));
    }

    public Long getTradingVolume() {
        return Math.min(sellingCount,buyingCount);
    }

    public void changeHits() {
        this.hits = hits + ThreadLocalRandom.current().nextLong(1L,3L);
    }
}
