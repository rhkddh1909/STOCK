package com.example.stockapi.api.stock;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockTopFiveAllRes<T> {
    private T stockTopFiveHits;
    private T stockTopFiveTradingVolume;
    private T stockTopFiveGrowthRate;
    private T stockBottomFiveGrowthRate;
}
