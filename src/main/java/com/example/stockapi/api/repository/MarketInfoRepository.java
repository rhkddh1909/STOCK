package com.example.stockapi.api.repository;

import com.example.stockapi.api.marketinfo.MarketInfoRes;
import com.example.stockapi.api.stockhistory.StockInfoHistoryDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.stockapi.api.repository.domain.QMarketInfo.marketInfo;
import static com.example.stockapi.api.repository.domain.QStockInfo.stockInfo;
import static com.example.stockapi.api.util.CUSTOM_CODE.RSEULT.CLOSE;

@Repository
@RequiredArgsConstructor
public class MarketInfoRepository {
    private final JPAQueryFactory queryFactory;

    /**nation을 입력받고 nation값을 체크하여 동적 쿼리 추가**/
    private BooleanExpression eqNationOrDefault(String nation){
        return Optional.ofNullable(nation)
                .map(item->marketInfo.marketNation.eq(nation.toUpperCase()))
                .orElse(marketInfo.marketNation.eq("KOR"));
    }

    /**marketCode를 입력받고 marketCode값을 체크하여 동적 쿼리 추가**/
    private BooleanExpression eqMarketCode(String marketCode){
        return Optional.ofNullable(marketCode)
                .map(item->stockInfo.marketCode.eq(marketCode.toUpperCase()))
                .orElse(null);
    }

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
                        .where(eqMarketCode(marketCode))
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
                .where(eqNationOrDefault(marketNation))
                .fetch());
    }

    public Optional<Long> bulkUpdateMarketOpen(String nation, String yn){
        return Optional.of(queryFactory
                .update(marketInfo)
                .set(marketInfo.marketOpenYn,yn)
                .set(marketInfo.marketSequence,marketInfo.marketSequence.add(yn.equals(CLOSE.CODE()) ? 1L : 0L))
                .where(
                        eqNationOrDefault(nation)
                        ,marketInfo.marketOpenYn.ne(yn)
                )
                .execute());
    }
}
