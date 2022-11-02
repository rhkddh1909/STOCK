package com.example.stockapi.api.repository.domain;

import lombok.EqualsAndHashCode;
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
    /**회차**/
    private Long marketSequence;

    public MarketInfo(String marketCode, String marketName, String marketOpenYn, String marketNation, Long marketSequence) {
        this.marketCode = marketCode;
        this.marketName = marketName;
        this.marketOpenYn = marketOpenYn;
        this.marketNation = marketNation;
        this.marketSequence = marketSequence;
    }

    public MarketInfo() {

    }


    public void updateMarketOpenYn(String marketOpenYn) {
        this.marketOpenYn = marketOpenYn;
        this.marketSequence = marketSequence + (marketOpenYn.equals("Y") ? 1L : 0L);
    }
}
