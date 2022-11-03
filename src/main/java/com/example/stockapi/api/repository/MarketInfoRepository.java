package com.example.stockapi.api.repository;

import com.example.stockapi.api.marketinfo.MarketInfoRes;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.stockapi.api.repository.domain.QMarketInfo.marketInfo;

@Repository
@RequiredArgsConstructor
public class MarketInfoRepository {
    private final JPAQueryFactory queryFactory;

    public Optional<List<MarketInfoRes>> findAll(){
        return Optional.of(queryFactory
                .select(Projections.fields(MarketInfoRes.class,
                        marketInfo.marketName
                        , marketInfo.marketOpenYn
                        , marketInfo.marketNation
                ))
                .from(marketInfo)
                .fetch());
    }

    public Optional<MarketInfoRes> findByMarketCode(String marketCode){
        return Optional.ofNullable(queryFactory
                        .select(Projections.fields(MarketInfoRes.class,
                                marketInfo.marketName
                                ,marketInfo.marketOpenYn
                                ,marketInfo.marketNation
                        ))
                        .from(marketInfo)
                        .where(marketInfo.marketCode.eq(marketCode))
                        .fetch())
                .orElse(List.of())
                .stream()
                .findFirst();
    }

    public Optional<List<MarketInfoRes>> findByMarketNation(String marketNation){
        return Optional.ofNullable(queryFactory
                .select(Projections.fields(MarketInfoRes.class,
                        marketInfo.marketName
                        ,marketInfo.marketOpenYn
                        ,marketInfo.marketNation
                ))
                .from(marketInfo)
                .where(marketInfo.marketNation.eq(marketNation))
                .fetch());
    }
}
