package com.example.stockapi.api.repository.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class StockInfoHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String marketCode;
    private String stockCode;
    private String stockName;
    private Long startingPrice;
    private Long endingPrice;
    private Long sellingCount;
    private Long buyingCount;
    private Long tradingVolume;
    private Long growthRate;
}
