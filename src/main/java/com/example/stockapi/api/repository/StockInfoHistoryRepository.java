package com.example.stockapi.api.repository;

import com.example.stockapi.api.stockhistory.StockInfoHistoryDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import static com.example.stockapi.api.repository.domain.QStockInfoHistory.stockInfoHistory;

@Repository
@RequiredArgsConstructor
public class StockInfoHistoryRepository {
    private final JPAQueryFactory queryFactory;

    private BooleanExpression eqNationOrDefault(String nation){
        return Optional.ofNullable(stockInfoHistory.marketNation.eq(nation))
                .orElse(stockInfoHistory.marketNation.eq("KOR"));
    }

    private BooleanExpression eqStockCode(String stockCode){
        return Optional.ofNullable(stockInfoHistory.marketNation.eq(stockCode))
                .orElse(null);
    }

    public Optional<List<StockInfoHistoryDto>> findByNationAndStockCode(String nation, String stockCode){
        return Optional.ofNullable(queryFactory
                .select(Projections.fields(StockInfoHistoryDto.class,
                        stockInfoHistory.stockInfoHistoryID.historyId
                        ,stockInfoHistory.stockInfoHistoryID.stockCode
                        ,stockInfoHistory.stockName
                        ,stockInfoHistory.marketCode
                        ,stockInfoHistory.marketName
                        ,stockInfoHistory.startingPrice
                        ,stockInfoHistory.endingPrice
                        ,stockInfoHistory.buyingCount
                        ,stockInfoHistory.sellingCount
                        ,stockInfoHistory.tradingVolume
                        ,stockInfoHistory.growthRate
                        ,stockInfoHistory.hits
                        ,stockInfoHistory.marketNation
                ))
                .from(stockInfoHistory)
                .where(eqNationOrDefault(nation)
                        ,eqStockCode(stockCode))
                .limit(365)
                .fetch());
    }
}
