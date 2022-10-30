package com.example.stockapi.api.repository;

import com.example.stockapi.api.exception.QueryNoExistException;
import com.example.stockapi.api.exception.StockNoExistException;
import com.example.stockapi.api.repository.domain.StockInfo;
import com.example.stockapi.api.stock.StockInfoRes;
import com.example.stockapi.api.stock.StockTopFiveAllRes;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.MathExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
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

    public Optional<List<StockInfo>> findAll(){
        return Optional.ofNullable(queryFactory
                .selectFrom(stockInfo)
                .fetch());
    }

    public Optional<List<StockInfo>> findHitsTopFive() {
        return Optional.ofNullable(queryFactory
                .selectFrom(stockInfo)
                .orderBy(stockInfo.hits.desc())
                .limit(5)
                .fetch());
    }

    public Optional<List<StockInfoRes>> findTradingVolumeTopFive() {
        return Optional.ofNullable(queryFactory
                .select(Projections.fields(StockInfoRes.class,
                        stockInfo.stockCode
                        , stockInfo.stockName
                        , stockInfo.startingPrice
                        , stockInfo.currentPrice
                        , stockInfo.hits
                        , new CaseBuilder().when(stockInfo.buyingCount.lt(stockInfo.sellingCount)).then(stockInfo.buyingCount).otherwise(stockInfo.sellingCount).as("tradingVolume")
                        , MathExpressions.round(stockInfo.currentPrice.doubleValue().subtract(stockInfo.currentPrice.doubleValue()).divide(stockInfo.startingPrice.doubleValue()).multiply(100.00),2).as("growthRate")
                ))
                .from(stockInfo)
                .orderBy(new CaseBuilder().when(stockInfo.buyingCount.lt(stockInfo.sellingCount)).then(stockInfo.buyingCount).otherwise(stockInfo.sellingCount).desc())
                .limit(5)
                .fetch());
    }

    public Optional<List<StockInfoRes>> findGrowthRateTopFive() {
        return Optional.ofNullable(queryFactory
                .select(Projections.fields(StockInfoRes.class,
                        stockInfo.stockCode
                        , stockInfo.stockName
                        , stockInfo.startingPrice
                        , stockInfo.currentPrice
                        , stockInfo.hits
                        , new CaseBuilder().when(stockInfo.buyingCount.lt(stockInfo.sellingCount)).then(stockInfo.buyingCount).otherwise(stockInfo.sellingCount).as("tradingVolume")
                        , MathExpressions.round(stockInfo.currentPrice.doubleValue().subtract(stockInfo.currentPrice.doubleValue()).divide(stockInfo.startingPrice.doubleValue()).multiply(100.00),2).as("growthRate")
                ))
                .from(stockInfo)
                .orderBy(MathExpressions.round(stockInfo.currentPrice.doubleValue().subtract(stockInfo.currentPrice.doubleValue()).divide(stockInfo.startingPrice.doubleValue()).multiply(100.00),2).desc())
                .limit(5)
                .fetch());
    }

    public Optional<List<StockInfoRes>> findGrowthRateBottomFive() {
        return Optional.ofNullable(queryFactory
                .select(Projections.fields(StockInfoRes.class,
                        stockInfo.stockCode
                        , stockInfo.stockName
                        , stockInfo.startingPrice
                        , stockInfo.currentPrice
                        , stockInfo.hits
                        , new CaseBuilder().when(stockInfo.buyingCount.lt(stockInfo.sellingCount)).then(stockInfo.buyingCount).otherwise(stockInfo.sellingCount).as("tradingVolume")
                        , MathExpressions.round(stockInfo.currentPrice.doubleValue().subtract(stockInfo.currentPrice.doubleValue()).divide(stockInfo.startingPrice.doubleValue()).multiply(100.00),2).as("growthRate")
                ))
                .from(stockInfo)
                .orderBy(MathExpressions.round(stockInfo.currentPrice.doubleValue().subtract(stockInfo.currentPrice.doubleValue()).divide(stockInfo.startingPrice.doubleValue()).multiply(100.00),2).asc())
                .limit(5)
                .fetch());
    }

    public Optional<StockTopFiveAllRes<List<StockInfoRes>>> findTopFiveAll() {
        StockTopFiveAllRes<List<StockInfoRes>> queryResult = new StockTopFiveAllRes<List<StockInfoRes>>();
        queryResult.setStockTopFiveHits(Optional.of(queryFactory
                .selectFrom(stockInfo)
                .orderBy(stockInfo.hits.desc())
                .limit(5)
                .fetch()
                .stream()
                .map(StockInfo::getStockInfoRes)
                .toList()).orElseThrow(()->new QueryNoExistException("cannot found HitsBottomFive in query")));

        queryResult.setStockTopFiveTradingVolume(Optional.ofNullable(queryFactory
                .select(Projections.fields(StockInfoRes.class,
                        stockInfo.stockCode
                        , stockInfo.stockName
                        , stockInfo.startingPrice
                        , stockInfo.currentPrice
                        , stockInfo.hits
                        , new CaseBuilder().when(stockInfo.buyingCount.lt(stockInfo.sellingCount)).then(stockInfo.buyingCount).otherwise(stockInfo.sellingCount).as("tradingVolume")
                        , MathExpressions.round(stockInfo.currentPrice.doubleValue().subtract(stockInfo.startingPrice.doubleValue()).divide(stockInfo.startingPrice.doubleValue()).multiply(100.00), 2).as("growthRate")
                ))
                .from(stockInfo)
                .orderBy(new CaseBuilder().when(stockInfo.buyingCount.lt(stockInfo.sellingCount)).then(stockInfo.buyingCount).otherwise(stockInfo.sellingCount).desc())
                .limit(5)
                .fetch()).orElseThrow(()->new QueryNoExistException("cannot found TradingVolumeTopFive in query")));

        queryResult.setStockTopFiveGrowthRate(Optional.ofNullable(queryFactory
                .select(Projections.fields(StockInfoRes.class,
                        stockInfo.stockCode
                        , stockInfo.stockName
                        , stockInfo.startingPrice
                        , stockInfo.currentPrice
                        , stockInfo.hits
                        , new CaseBuilder().when(stockInfo.buyingCount.lt(stockInfo.sellingCount)).then(stockInfo.buyingCount).otherwise(stockInfo.sellingCount).as("tradingVolume")
                        , MathExpressions.round(stockInfo.currentPrice.doubleValue().subtract(stockInfo.startingPrice.doubleValue()).divide(stockInfo.startingPrice.doubleValue()).multiply(100.00), 2).as("growthRate")
                ))
                .from(stockInfo)
                .orderBy(MathExpressions.round(stockInfo.currentPrice.doubleValue().subtract(stockInfo.startingPrice.doubleValue()).divide(stockInfo.startingPrice.doubleValue()).multiply(100.00), 2).desc())
                .limit(5)
                .fetch()).orElseThrow(()->new QueryNoExistException("cannot found GrowthRateTopFive in query")));

        queryResult.setStockBottomFiveGrowthRate(Optional.ofNullable(queryFactory
                .select(Projections.fields(StockInfoRes.class,
                        stockInfo.stockCode
                        , stockInfo.stockName
                        , stockInfo.startingPrice
                        , stockInfo.currentPrice
                        , stockInfo.hits
                        , new CaseBuilder().when(stockInfo.buyingCount.lt(stockInfo.sellingCount)).then(stockInfo.buyingCount).otherwise(stockInfo.sellingCount).as("tradingVolume")
                        , MathExpressions.round(stockInfo.currentPrice.doubleValue().subtract(stockInfo.startingPrice.doubleValue()).divide(stockInfo.startingPrice.doubleValue()).multiply(100.00), 2).as("growthRate")
                ))
                .from(stockInfo)
                .orderBy(MathExpressions.round(stockInfo.currentPrice.doubleValue().subtract(stockInfo.startingPrice.doubleValue()).divide(stockInfo.startingPrice.doubleValue()).multiply(100.00), 2).asc())
                .limit(5)
                .fetch()).orElseThrow(()->new QueryNoExistException("cannot found GrowthRateBottomFive in query")));

        return Optional.of(queryResult);
    }

    public List<StockInfoRes> findDetailTopHits(Pageable pageable) {
        List<StockInfoRes> hitsListDetail = Optional.of(queryFactory
                        .selectFrom(stockInfo)
                        .orderBy(stockInfo.hits.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch()
                .stream()
                .map(StockInfo::getStockInfoRes)
                .toList())
                .orElseThrow(()->new QueryNoExistException("cannot found GrowthRateBottomFive in query"));

        return Optional.of(PageableExecutionUtils.getPage(hitsListDetail,pageable,() -> {return 100;}).get().collect(Collectors.toList())).orElseThrow(()->new StockNoExistException("connot found StockInfos"));
    }

    public List<StockInfoRes> findDetailTopTradingVolume(Pageable pageable) {
        List<StockInfoRes> tradingVolumeListDetail = Optional.ofNullable(queryFactory
                        .select(Projections.fields(StockInfoRes.class,
                                stockInfo.stockCode
                                , stockInfo.stockName
                                , stockInfo.startingPrice
                                , stockInfo.currentPrice
                                , stockInfo.hits
                                , new CaseBuilder().when(stockInfo.buyingCount.lt(stockInfo.sellingCount)).then(stockInfo.buyingCount).otherwise(stockInfo.sellingCount).as("tradingVolume")
                                , MathExpressions.round(stockInfo.currentPrice.doubleValue().subtract(stockInfo.startingPrice.doubleValue()).divide(stockInfo.startingPrice.doubleValue()).multiply(100.00),2).as("growthRate")
                        ))
                        .from(stockInfo)
                        .orderBy(new CaseBuilder().when(stockInfo.buyingCount.lt(stockInfo.sellingCount)).then(stockInfo.buyingCount).otherwise(stockInfo.sellingCount).desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch())
                .orElseThrow(()->new QueryNoExistException("cannot found GrowthRateBottomFive in query"));

        return Optional.of(PageableExecutionUtils.getPage(tradingVolumeListDetail,pageable,() -> {return 100;}).get().collect(Collectors.toList())).orElseThrow(()->new StockNoExistException("connot found StockInfos"));
    }

    public List<StockInfoRes> findDetailTopGrowthRate(Pageable pageable) {
        List<StockInfoRes> growthRateList = Optional.ofNullable(queryFactory
                        .select(Projections.fields(StockInfoRes.class,
                                stockInfo.stockCode
                                , stockInfo.stockName
                                , stockInfo.startingPrice
                                , stockInfo.currentPrice
                                , stockInfo.hits
                                , new CaseBuilder().when(stockInfo.buyingCount.lt(stockInfo.sellingCount)).then(stockInfo.buyingCount).otherwise(stockInfo.sellingCount).as("tradingVolume")
                                , MathExpressions.round(stockInfo.currentPrice.doubleValue().subtract(stockInfo.startingPrice.doubleValue()).divide(stockInfo.startingPrice.doubleValue()).multiply(100.00),2).as("growthRate")
                        ))
                        .from(stockInfo)
                        .orderBy(MathExpressions.round(stockInfo.currentPrice.doubleValue().subtract(stockInfo.startingPrice.doubleValue()).divide(stockInfo.startingPrice.doubleValue()).multiply(100.00),2).desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch())
                .orElseThrow(()->new QueryNoExistException("cannot found GrowthRateBottomFive in query"));

        return Optional.of(PageableExecutionUtils.getPage(growthRateList,pageable,() -> {return 100;}).get().collect(Collectors.toList())).orElseThrow(()->new StockNoExistException("connot found StockInfos"));
    }

    public List<StockInfoRes> findDetailBottomGrowthRate(Pageable pageable) {
        List<StockInfoRes> growthRateList = Optional.ofNullable(queryFactory
                        .select(Projections.fields(StockInfoRes.class,
                                stockInfo.stockCode
                                , stockInfo.stockName
                                , stockInfo.startingPrice
                                , stockInfo.currentPrice
                                , stockInfo.hits
                                , new CaseBuilder().when(stockInfo.buyingCount.lt(stockInfo.sellingCount)).then(stockInfo.buyingCount).otherwise(stockInfo.sellingCount).as("tradingVolume")
                                , MathExpressions.round(stockInfo.currentPrice.doubleValue().subtract(stockInfo.startingPrice.doubleValue()).divide(stockInfo.startingPrice.doubleValue()).multiply(100.00),2).as("growthRate")
                        ))
                        .from(stockInfo)
                        .orderBy(MathExpressions.round(stockInfo.currentPrice.doubleValue().subtract(stockInfo.startingPrice.doubleValue()).divide(stockInfo.startingPrice.doubleValue()).multiply(100.00),2).asc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch())
                .orElseThrow(()->new QueryNoExistException("cannot found GrowthRateBottomFive in query"));

        return Optional.of(PageableExecutionUtils.getPage(growthRateList,pageable,() -> {return 100;}).get().collect(Collectors.toList())).orElseThrow(()->new StockNoExistException("connot found StockInfos"));
    }
}
