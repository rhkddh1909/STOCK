package com.example.stockapi.api.repository.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class StockInfoHistoryID implements Serializable {
    /**
     * 회차
     **/
    private Long historyId;
    /**
     * 종목코드
     **/
    private String stockCode;
}
