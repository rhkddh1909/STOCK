package com.example.stockapi.api.repository.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class StockInfoHistoryId implements Serializable {
    /**
     * 회차
     **/
    private Long historyId;
    /**
     * 종목코드
     **/
    private String stockCode;

    public StockInfoHistoryId(Long historyId, String stockCode) {
        this.historyId = historyId;
        this.stockCode = stockCode;
    }
}
