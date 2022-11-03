package com.example.stockapi.api.repository;

import com.example.stockapi.api.exception.QueryNoExistException;
import com.example.stockapi.api.stockhistory.StockInfoHistoryDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.stockapi.api.repository.domain.QMarketInfo.marketInfo;
import static com.example.stockapi.api.repository.domain.QStockInfoHistory.stockInfoHistory;

@Repository
@RequiredArgsConstructor
public class StockInfoHistoryRepository {
    private final JPAQueryFactory queryFactory;
    private final StringPath aliasGrowthRate = Expressions.stringPath("growthRate");
    private BooleanExpression eqNationOrDefault(String nation){
        return Optional.ofNullable(nation)
                .map(item->stockInfoHistory.marketNation.eq(nation.toUpperCase()))
                .orElse(stockInfoHistory.marketNation.eq("KOR"));

    }

    private BooleanExpression eqStockCode(String stockCode){
        return Optional.ofNullable(stockCode)
                .map(item->stockInfoHistory.marketCode.eq(stockCode))
                .orElse(null);
    }

    private BooleanExpression eqMarketNationOrDefault(String nation){
        return Optional.ofNullable(nation)
                .map(item->marketInfo.marketNation.eq(nation.toUpperCase()))
                .orElse(marketInfo.marketNation.eq("KOR"));
    }

    private NumberExpression<Long> getStartingPrice(Long sequence, Long analsisTerm){
        return new CaseBuilder()
                .when(stockInfoHistory.historyId.eq(sequence-analsisTerm))
                .then(stockInfoHistory.startingPrice)
                .otherwise(0L);
    }

    private NumberExpression<Long> getEndingPrice(Long sequence){
        return new CaseBuilder()
                .when(stockInfoHistory.historyId.eq(sequence))
                .then(stockInfoHistory.endingPrice)
                .otherwise(0L);
    }

    public Optional<List<StockInfoHistoryDto>> findByNationAndStockCode(String nation, String stockCode){
        return Optional.ofNullable(queryFactory
                .select(Projections.fields(StockInfoHistoryDto.class,
                        stockInfoHistory.historyId
                        ,stockInfoHistory.stockCode
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
    public Optional<List<StockInfoHistoryDto>> findStockInfoHistoryGroupData(String nation, String stockCode, Long analsisTerm){
        Long sequence = queryFactory
                .selectFrom(marketInfo)
                .where(eqMarketNationOrDefault(nation))
                .limit(1)
                .fetch()
                .stream()
                .mapToLong(item->item.getMarketSequence())
                .filter(item->item!=0L)
                .findFirst().orElseThrow(()->new QueryNoExistException("거래이력이 없습니다."));

        return Optional.ofNullable(queryFactory
                .select(Projections.fields(StockInfoHistoryDto.class
                        ,stockInfoHistory.stockCode
                        ,stockInfoHistory.stockName
                        ,stockInfoHistory.marketName
                        ,getStartingPrice(sequence,analsisTerm).sum().as("startingPrice")
                        ,getEndingPrice(sequence).sum().as("endingPrice")
                        ,stockInfoHistory.buyingCount.sum().as("buyingCount")
                        ,stockInfoHistory.sellingCount.sum().as("sellingCount")
                        ,stockInfoHistory.tradingVolume.sum().as("tradingVolume")
                        ,stockInfoHistory.hits.sum().as("hits")
                        ,stockInfoHistory.marketNation
                        ,MathExpressions.round(
                                getEndingPrice(sequence).sum().doubleValue()
                                        .subtract(getStartingPrice(sequence,analsisTerm).sum().doubleValue())
                                        .divide(getStartingPrice(sequence,analsisTerm).sum().doubleValue())
                                        .multiply(100.00),2).as("growthRate")
                ))
                .from(stockInfoHistory)
                .where(
                        eqNationOrDefault(nation)
                        ,eqStockCode(stockCode)
                        ,stockInfoHistory.historyId.goe(sequence-analsisTerm)
                )
                .groupBy(
                        stockInfoHistory.stockCode
                        ,stockInfoHistory.stockName
                        ,stockInfoHistory.marketName
                        ,stockInfoHistory.marketNation
                )
                .orderBy(aliasGrowthRate.desc())
                .limit(20)
                .fetch());
    }
}
