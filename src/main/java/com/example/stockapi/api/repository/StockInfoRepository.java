package com.example.stockapi.api.repository;

import com.example.stockapi.api.exception.QueryNoExistException;
import com.example.stockapi.api.repository.domain.StockInfo;
import com.example.stockapi.api.stock.StockInfoRes;
import com.example.stockapi.api.stock.StockTopFiveAllRes;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.stockapi.api.repository.domain.QStockInfo.stockInfo;

@Repository
@RequiredArgsConstructor
public class StockInfoRepository{
    private final JPAQueryFactory queryFactory;

    private final StringPath aliasTradingVolume = Expressions.stringPath("tradingVolume");
    private final StringPath aliasGrowthRate = Expressions.stringPath("growthRate");

    private BooleanExpression eqNationOrDefault(String nation){
        return Optional.ofNullable(nation)
                .map(item->stockInfo.marketNation.eq(nation.toUpperCase()))
                .orElse(stockInfo.marketNation.eq("KOR"));
    }
    public Optional<List<StockInfo>> findByNation(String nation){
        return Optional.ofNullable(queryFactory
                .selectFrom(stockInfo)
                .where(eqNationOrDefault(nation))
                .fetch());
    }

    public StockTopFiveAllRes<List<StockInfoRes>> findTopFiveAll(String nation) {

        StockTopFiveAllRes<List<StockInfoRes>> queryResult = new StockTopFiveAllRes<>();
        Pageable pageable = PageRequest.of(0,5);

        queryResult.setStockTopFiveHits(findDetailTopHits(pageable,nation).orElseThrow(()->new QueryNoExistException("cannot found StockInfo")));
        queryResult.setStockTopFiveTradingVolume(findDetailTopTradingVolume(pageable,nation).orElseThrow(()->new QueryNoExistException("cannot found StockInfo")));
        queryResult.setStockTopFiveGrowthRate(findDetailTopGrowthRate(pageable,nation).orElseThrow(()->new QueryNoExistException("cannot found StockInfo")));
        queryResult.setStockBottomFiveGrowthRate(findDetailBottomGrowthRate(pageable,nation).orElseThrow(()->new QueryNoExistException("cannot found StockInfo")));

        return queryResult;
    }

    public Optional<List<StockInfoRes>> findDetailTopHits(Pageable pageable,String nation) {
        List<StockInfoRes> hitsListDetail = Optional.of(queryFactory
                        .selectFrom(stockInfo)
                        .where(eqNationOrDefault(nation))
                        .orderBy(stockInfo.hits.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch()
                .stream()
                .map(StockInfo::getStockInfoRes)
                .toList())
                .orElse(List.of());

        return Optional.of(PageableExecutionUtils.getPage(hitsListDetail,pageable,() -> 100).get().collect(Collectors.toList()));
    }

    public Optional<List<StockInfoRes>> findDetailTopTradingVolume(Pageable pageable, String nation) {
        List<StockInfoRes> tradingVolumeListDetail = Optional.ofNullable(queryFactory
                        .select(Projections.fields(StockInfoRes.class,
                                stockInfo.stockCode
                                , stockInfo.stockName
                                , stockInfo.marketName
                                , stockInfo.startingPrice
                                , stockInfo.currentPrice
                                , stockInfo.hits
                                , new CaseBuilder().when(stockInfo.buyingCount.lt(stockInfo.sellingCount))
                                        .then(stockInfo.buyingCount)
                                        .otherwise(stockInfo.sellingCount).as("tradingVolume")
                                , MathExpressions.round(
                                        stockInfo.currentPrice.doubleValue()
                                                .subtract(stockInfo.startingPrice.doubleValue())
                                                .divide(stockInfo.startingPrice.doubleValue())
                                                .multiply(100.00),2).as("growthRate")
                        ))
                        .from(stockInfo)
                        .where(eqNationOrDefault(nation))
                        .orderBy(aliasTradingVolume.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch())
                .orElse(List.of());

        return Optional.of(PageableExecutionUtils.getPage(tradingVolumeListDetail,pageable,() -> 100).get().collect(Collectors.toList()));
    }

    public Optional<List<StockInfoRes>> findDetailTopGrowthRate(Pageable pageable, String nation) {
        List<StockInfoRes> growthRateList = Optional.ofNullable(queryFactory
                        .select(Projections.fields(StockInfoRes.class,
                                stockInfo.stockCode
                                , stockInfo.stockName
                                , stockInfo.marketName
                                , stockInfo.startingPrice
                                , stockInfo.currentPrice
                                , stockInfo.hits
                                , new CaseBuilder().when(stockInfo.buyingCount.lt(stockInfo.sellingCount))
                                        .then(stockInfo.buyingCount)
                                        .otherwise(stockInfo.sellingCount).as("tradingVolume")
                                , MathExpressions.round(stockInfo.currentPrice.doubleValue()
                                        .subtract(stockInfo.startingPrice.doubleValue())
                                        .divide(stockInfo.startingPrice.doubleValue())
                                        .multiply(100.00),2).as("growthRate")
                        ))
                        .from(stockInfo)
                        .where(eqNationOrDefault(nation))
                        .orderBy(aliasGrowthRate.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch())
                .orElse(List.of());

        return Optional.of(PageableExecutionUtils.getPage(growthRateList,pageable,() -> 100).get().collect(Collectors.toList()));
    }

    public Optional<List<StockInfoRes>> findDetailBottomGrowthRate(Pageable pageable, String nation) {
        List<StockInfoRes> growthRateList = Optional.ofNullable(queryFactory
                        .select(Projections.fields(StockInfoRes.class,
                                stockInfo.stockCode
                                , stockInfo.stockName
                                , stockInfo.marketName
                                , stockInfo.startingPrice
                                , stockInfo.currentPrice
                                , stockInfo.hits
                                , new CaseBuilder().when(stockInfo.buyingCount.lt(stockInfo.sellingCount))
                                        .then(stockInfo.buyingCount)
                                        .otherwise(stockInfo.sellingCount).as("tradingVolume")
                                , MathExpressions.round(stockInfo.currentPrice.doubleValue()
                                        .subtract(stockInfo.startingPrice.doubleValue())
                                        .divide(stockInfo.startingPrice.doubleValue())
                                        .multiply(100.00),2).as("growthRate")
                        ))
                        .from(stockInfo)
                        .where(eqNationOrDefault(nation))
                        .orderBy(aliasGrowthRate.asc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch())
                .orElse(List.of());

        return Optional.of(PageableExecutionUtils.getPage(growthRateList,pageable,() -> 100).get().collect(Collectors.toList()));
    }
}
