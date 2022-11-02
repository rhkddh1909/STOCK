package com.example.stockapi.api.repository.domain;

import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@DynamicUpdate
public class MarketInfo {
    /**시장코드**/
    @Id
    private String marketCode;
    /**시장명**/
    private String marketName;
    /**시장오픈여부**/
    private String marketOpenYn;
    /**국가**/
    private String marketNation;

    public MarketInfo(String marketCode, String marketName, String marketOpenYn, String marketNation) {
        this.marketCode = marketCode;
        this.marketName = marketName;
        this.marketOpenYn = marketOpenYn;
        this.marketNation = marketNation;
    }


    public void updateMarketOpenYn(String marketOpenYn) {
        this.marketOpenYn = marketOpenYn;
    }
}
