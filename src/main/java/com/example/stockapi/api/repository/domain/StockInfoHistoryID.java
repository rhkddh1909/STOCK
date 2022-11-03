package com.example.stockapi.api.repository.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
public class StockInfoHistoryID implements Serializable {
    /**
     * 회차
     **/
    private Long historyId;
    /**
     * 종목코드
     **/
    private String stockCode;

    public StockInfoHistoryID(Long historyId, String stockCode) {
        this.historyId = historyId;
        this.stockCode = stockCode;
    }
}
