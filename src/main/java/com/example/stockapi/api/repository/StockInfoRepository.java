package com.example.stockapi.api.repository;

import com.example.stockapi.api.exception.QueryNoExistException;
import com.example.stockapi.api.repository.domain.StockInfo;
import com.example.stockapi.api.repository.domain.StockInfoHistory;
import com.example.stockapi.api.stock.StockInfoRes;
import com.example.stockapi.api.stock.StockTopFiveAllRes;
import com.example.stockapi.api.stockhistory.StockInfoHistoryDto;
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

import static com.example.stockapi.api.repository.domain.QMarketInfo.marketInfo;
import static com.example.stockapi.api.repository.domain.QStockInfo.stockInfo;

@Repository
@RequiredArgsConstructor
public class StockInfoRepository{
    private final JPAQueryFactory queryFactory;

    private final StringPath aliasTradingVolume = Expressions.stringPath("tradingVolume");
    private final StringPath aliasGrowthRate = Expressions.stringPath("growthRate");

    /**nation을 입력받고 nation값을 체크하여 동적 쿼리 추가**/
    private BooleanExpression eqNationOrDefault(String nation){
        return Optional.ofNullable(nation)
                .map(item->stockInfo.marketNation.eq(nation.toUpperCase()))
                .orElse(stockInfo.marketNation.eq("KOR"));
    }

    /**marketCode를 입력받고 marketCode값을 체크하여 동적 쿼리 추가**/
    private BooleanExpression eqMarketCode(String marketCode){
        return Optional.ofNullable(marketCode)
                .map(item->stockInfo.marketCode.eq(marketCode.toUpperCase()))
                .orElse(null);
    }

    public Optional<List<StockInfo>> findByNationOnly(String nation){
        return Optional.ofNullable(queryFactory
                .selectFrom(stockInfo)
                .where(
                        eqNationOrDefault(nation)
                )
                .fetch());
    }

    public Optional<List<StockInfo>> findByNation(String nation, String marketCode){
        return Optional.ofNullable(queryFactory
                .selectFrom(stockInfo)
                .where(
                        eqNationOrDefault(nation)
                        ,eqMarketCode(marketCode)
                )
                .fetch());
    }

    public StockTopFiveAllRes<List<StockInfoRes>> findTopFiveAll(String nation, String marketCode) {

        StockTopFiveAllRes<List<StockInfoRes>> queryResult = new StockTopFiveAllRes<>();
        Pageable pageable = PageRequest.of(0,5);

        queryResult.setStockTopFiveHits(findDetailTopHits(pageable,nation,marketCode).orElseThrow(()->new QueryNoExistException("cannot found StockInfo")));
        queryResult.setStockTopFiveTradingVolume(findDetailTopTradingVolume(pageable,nation,marketCode).orElseThrow(()->new QueryNoExistException("cannot found StockInfo")));
        queryResult.setStockTopFiveGrowthRate(findDetailTopGrowthRate(pageable,nation,marketCode).orElseThrow(()->new QueryNoExistException("cannot found StockInfo")));
        queryResult.setStockBottomFiveGrowthRate(findDetailBottomGrowthRate(pageable,nation,marketCode).orElseThrow(()->new QueryNoExistException("cannot found StockInfo")));

        return queryResult;
    }

    public Optional<List<StockInfoRes>> findDetailTopHits(Pageable pageable,String nation, String marketCode) {
        List<StockInfoRes> hitsListDetail = Optional.of(queryFactory
                        .selectFrom(stockInfo)
                        .where(
                                eqNationOrDefault(nation)
                                ,eqMarketCode(marketCode)
                        )
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

    public Optional<List<StockInfoRes>> findDetailTopTradingVolume(Pageable pageable, String nation, String marketCode) {
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
                        .where(
                                eqNationOrDefault(nation)
                                ,eqMarketCode(marketCode)
                        )
                        .orderBy(aliasTradingVolume.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch())
                .orElse(List.of());

        return Optional.of(PageableExecutionUtils.getPage(tradingVolumeListDetail,pageable,() -> 100).get().collect(Collectors.toList()));
    }

    public Optional<List<StockInfoRes>> findDetailTopGrowthRate(Pageable pageable, String nation, String marketCode) {
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
                        .where(
                                eqNationOrDefault(nation)
                                ,eqMarketCode(marketCode)
                        )
                        .orderBy(aliasGrowthRate.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch())
                .orElse(List.of());

        return Optional.of(PageableExecutionUtils.getPage(growthRateList,pageable,() -> 100).get().collect(Collectors.toList()));
    }

    public Optional<List<StockInfoRes>> findDetailBottomGrowthRate(Pageable pageable, String nation, String marketCode) {
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
                        .where(
                                eqNationOrDefault(nation)
                                ,eqMarketCode(marketCode)
                        )
                        .orderBy(aliasGrowthRate.asc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch())
                .orElse(List.of());

        return Optional.of(PageableExecutionUtils.getPage(growthRateList,pageable,() -> 100).get().collect(Collectors.toList()));
    }

    public Optional<List<StockInfoHistory>> selectStockInfoHistorys(String nation){
        return Optional.ofNullable(queryFactory.select(Projections.fields(StockInfoHistory.class,
                        marketInfo.marketSequence.as("historyId")
                        , stockInfo.stockCode
                        , stockInfo.stockName
                        , marketInfo.marketCode
                        , marketInfo.marketName
                        , stockInfo.startingPrice
                        , stockInfo.currentPrice.as("endingPrice")
                        , stockInfo.sellingCount
                        , stockInfo.buyingCount
                        , stockInfo.hits
                        , marketInfo.marketNation
                        , new CaseBuilder().when(stockInfo.buyingCount.lt(stockInfo.sellingCount)).then(stockInfo.buyingCount).otherwise(stockInfo.sellingCount).as("tradingVolume")
                        , MathExpressions.round(stockInfo.currentPrice.doubleValue().subtract(stockInfo.startingPrice.doubleValue()).divide(stockInfo.startingPrice.doubleValue()).multiply(100.00),2).as("growthRate")
                ))
                .from(stockInfo)
                .join(marketInfo)
                .on(stockInfo.marketCode.eq(marketInfo.marketCode))
                .where(eqNationOrDefault(nation))
                .fetch());
    }
}
