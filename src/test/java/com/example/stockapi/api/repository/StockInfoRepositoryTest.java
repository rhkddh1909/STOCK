package com.example.stockapi.api.repository;

import com.example.stockapi.api.repository.domain.StockInfo;
import com.example.stockapi.api.stock.StockInfoRes;
import com.example.stockapi.api.stock.StockTopFiveAllRes;
import com.example.stockapi.api.util.Util;
import com.example.stockapi.config.QuerydslTestConfiguration;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.stockapi.api.repository.domain.QStockInfo.stockInfo;
import static com.example.stockapi.api.util.Util.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QuerydslTestConfiguration.class)
class StockInfoRepositoryTest {
    private StockInfoRepository testStockInfoRepository;
    @Autowired
    private JPAQueryFactory queryFactory;

    private final String nation = "KOR";

    public BooleanExpression eqNationOrDefault(String nation){
        if(StringUtils.isBlank(nation)){
            return stockInfo.marketNation.eq("KOR");
        }
        else{
            return stockInfo.marketNation.eq(nation);
        }
    }

    @Test
    public void selectStockInfo_findAllTest(){
        assertThat(Optional.of(queryFactory
                        .selectFrom(stockInfo)
                        .where(eqNationOrDefault(nation))
                .fetch())
                .orElse(List.of()))
                .isNotEmpty();
    }

    @Test
    @Transactional
    public void updateStockInfo_HitsTest(){
        Optional.of(queryFactory
                .selectFrom(stockInfo)
                .where(eqNationOrDefault(nation))
                .fetch())
                .orElse(List.of())
                .stream()
                .forEach(item->item.reRanking());
        Optional.of(queryFactory
                .selectFrom(stockInfo)
                .fetch())
                .orElse(List.of())
                .stream()
                .forEach(item->assertThat(item.getHits()).isBetween(Util.checkGrowthRate.apply(Util.getGrowthRate(item.getStartingPrice(),item.getCurrentPrice()))/10L, Util.checkGrowthRate.apply(Util.getGrowthRate(item.getStartingPrice(),item.getCurrentPrice()))));
    }

    @Test
    @Transactional
    public void updateStockInfo_reRankingTest(){
        List<StockInfo> stockInfoList = Optional.of(queryFactory
                .selectFrom(stockInfo)
                .where(eqNationOrDefault(nation))
                .fetch())
                .orElse(List.of());
        List<Long> askingPriceList = stockInfoList.stream()
                .map(item-> checkPrice.apply(item.getCurrentPrice()))
                .toList();
        List<Long> checkGrowthRateList = stockInfoList.stream().mapToLong(info -> checkGrowthRate.apply(getGrowthRate(info.getStartingPrice(), info.getCurrentPrice())))
                .boxed()
                .toList();

        stockInfoList.forEach(StockInfo::reRanking);

        List<StockInfo> stockInfoAfterList = Optional.of(queryFactory.selectFrom(stockInfo).fetch()).orElse(List.of());

        IntStream.range(0,stockInfoList.size()).forEach(item->{
            assertThat(stockInfoAfterList.get(item).getCurrentPrice())
                    .isBetween(stockInfoAfterList.get(item).getStartingPrice()+(askingPriceList.get(item)*-5)
                            ,stockInfoAfterList.get(item).getStartingPrice()+(askingPriceList.get(item)*5));
            assertThat(stockInfoAfterList.get(item).getHits()).isBetween(checkGrowthRateList.get(item)/10,checkGrowthRateList.get(item));
            assertThat(stockInfoAfterList.get(item).getBuyingCount()).isBetween(checkGrowthRateList.get(item)/10,checkGrowthRateList.get(item)*5);
            assertThat(stockInfoAfterList.get(item).getSellingCount()).isBetween(checkGrowthRateList.get(item)/10,checkGrowthRateList.get(item)*5);
        });
    }

    @Test
    public void selectStockInfo_findHitsTopFiveTest(){
        List<StockInfo> stockInfoList = Optional.of(queryFactory.selectFrom(stockInfo).fetch()).orElse(List.of());
        stockInfoList.forEach(StockInfo::updateCurrPrice);
        List<Long> hitsList = Optional.ofNullable(queryFactory
                .selectFrom(stockInfo)
                .where(eqNationOrDefault(nation))
                .orderBy(stockInfo.hits.desc())
                .limit(5)
                .fetch())
                .orElse(List.of())
                .stream()
                .mapToLong(StockInfo::getHits)
                .boxed()
                .collect(Collectors.toList());

        List<Long> tmpHitsList = new ArrayList<Long>(hitsList);
        Collections.sort(hitsList,Collections.reverseOrder());
        assertThat(tmpHitsList).isEqualTo(hitsList);
        assertThat(hitsList.size()).isEqualTo(5);
    }

    @Test
    public void selectStockInfo_findTradingVolumeTopFiveTest(){
        List<StockInfo> stockInfoList = Optional.of(queryFactory.selectFrom(stockInfo).where(eqNationOrDefault(nation)).fetch()).orElse(List.of());
        stockInfoList.forEach(StockInfo::reRanking);
        StringPath aliasTradingVolume = Expressions.stringPath("tradingVolume");

        List<Long> tradingVolumeList = Optional.ofNullable(queryFactory
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
                .where(eqNationOrDefault(nation))
                .orderBy(aliasTradingVolume.desc())
                .limit(5)
                .fetch())
                .orElse(List.of())
                .stream()
                .mapToLong(StockInfoRes::getTradingVolume)
                .boxed()
                .collect(Collectors.toList());

        List<Long> tmpTradingVolumeList = new ArrayList<Long>(tradingVolumeList);
        Collections.sort(tradingVolumeList,Collections.reverseOrder());
        assertThat(tradingVolumeList).isNotEmpty();
        assertThat(tmpTradingVolumeList).isEqualTo(tradingVolumeList);
        assertThat(tradingVolumeList.size()).isEqualTo(5);
    }

    @Test
    public void selectStockInfo_findGrowthRateTopFiveTest(){
        List<StockInfo> stockInfoList = Optional.of(queryFactory.selectFrom(stockInfo).where(eqNationOrDefault(nation)).fetch()).orElse(List.of());
        stockInfoList.forEach(StockInfo::reRanking);
        StringPath aliasGrowthRate = Expressions.stringPath("growthRate");

        List<Double> growthRateList = Optional.ofNullable(queryFactory
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
                .where(eqNationOrDefault(nation))
                .orderBy(aliasGrowthRate.desc())
                .limit(5)
                .fetch())
                .orElse(List.of())
                .stream()
                .mapToDouble(StockInfoRes::getGrowthRate)
                .boxed()
                .collect(Collectors.toList());

        List<Double> tmpGrowthRateList = new ArrayList<Double>(growthRateList);
        growthRateList.sort(Collections.reverseOrder());
        assertThat(growthRateList).isNotEmpty();
        assertThat(tmpGrowthRateList).isEqualTo(growthRateList);
        assertThat(growthRateList.size()).isEqualTo(5);
    }

    @Test
    public void selectStockInfo_findGrowthRateBottomFiveTest(){
        List<StockInfo> stockInfoList = Optional.of(queryFactory.selectFrom(stockInfo).where(eqNationOrDefault(nation)).fetch()).orElse(List.of());
        stockInfoList.forEach(StockInfo::reRanking);

        StringPath aliasGrowthRate = Expressions.stringPath("growthRate");
        List<Double> growthRateList = Optional.ofNullable(queryFactory
                        .select(Projections.fields(StockInfoRes.class,
                                stockInfo.stockCode
                                , stockInfo.stockName
                                , stockInfo.startingPrice
                                , stockInfo.currentPrice
                                , stockInfo.hits
                                , new CaseBuilder().when(stockInfo.buyingCount.lt(stockInfo.sellingCount)).then(stockInfo.buyingCount).otherwise(stockInfo.sellingCount).as("tradingVolume")
                                , MathExpressions.round(stockInfo.currentPrice.doubleValue().subtract(stockInfo.currentPrice.doubleValue()).divide(stockInfo.startingPrice.doubleValue()).multiply(100.00), 2).as("growthRate")
                        ))
                        .from(stockInfo)
                        .where(eqNationOrDefault(nation))
                        .orderBy(aliasGrowthRate.asc())
                        .limit(5)
                        .fetch())
                .orElse(List.of())
                .stream()
                .mapToDouble(StockInfoRes::getGrowthRate)
                .boxed()
                .collect(Collectors.toList());

        List<Double> tmpGrowthRateList = new ArrayList<>(growthRateList);
        Collections.sort(growthRateList);
        assertThat(growthRateList).isNotEmpty();
        assertThat(growthRateList.size()).isEqualTo(5);
    }

    @Test
    public void selectStockInfo_findTopFiveAllTest(){
        List<StockInfo> stockInfoList = Optional.of(queryFactory.selectFrom(stockInfo).where(eqNationOrDefault(nation)).fetch()).orElse(List.of());
        stockInfoList.forEach(StockInfo::reRanking);

        StringPath aliasTradingVolume = Expressions.stringPath("tradingVolume");
        StringPath aliasGrowthRate = Expressions.stringPath("growthRate");

        StockTopFiveAllRes<List<StockInfoRes>> queryResult = new StockTopFiveAllRes<List<StockInfoRes>>();
        queryResult.setStockTopFiveHits(Optional.of(queryFactory
                .selectFrom(stockInfo)
                .where(eqNationOrDefault(nation))
                .orderBy(stockInfo.hits.desc())
                .limit(5)
                .fetch()
                .stream()
                .map(StockInfo::getStockInfoRes)
                .toList()).orElse(List.of()));

        queryResult.setStockTopFiveTradingVolume(Optional.ofNullable(queryFactory
                .select(Projections.fields(StockInfoRes.class,
                        stockInfo.stockCode
                        , stockInfo.stockName
                        , stockInfo.startingPrice
                        , stockInfo.currentPrice
                        , stockInfo.hits
                        , new CaseBuilder().when(stockInfo.buyingCount.lt(stockInfo.sellingCount)).then(stockInfo.buyingCount).otherwise(stockInfo.sellingCount).as("tradingVolume")
                        , MathExpressions.round(stockInfo.currentPrice.doubleValue().subtract(stockInfo.currentPrice.doubleValue()).divide(stockInfo.startingPrice.doubleValue()).multiply(100.00), 2).as("growthRate")
                ))
                .from(stockInfo)
                .where(eqNationOrDefault(nation))
                .orderBy(aliasTradingVolume.desc())
                .limit(5)
                .fetch()).orElse(List.of()));

        queryResult.setStockTopFiveGrowthRate(Optional.ofNullable(queryFactory
                .select(Projections.fields(StockInfoRes.class,
                        stockInfo.stockCode
                        , stockInfo.stockName
                        , stockInfo.startingPrice
                        , stockInfo.currentPrice
                        , stockInfo.hits
                        , new CaseBuilder().when(stockInfo.buyingCount.lt(stockInfo.sellingCount)).then(stockInfo.buyingCount).otherwise(stockInfo.sellingCount).as("tradingVolume")
                        , MathExpressions.round(stockInfo.currentPrice.doubleValue().subtract(stockInfo.currentPrice.doubleValue()).divide(stockInfo.startingPrice.doubleValue()).multiply(100.00), 2).as("growthRate")
                ))
                .from(stockInfo)
                .where(eqNationOrDefault(nation))
                .orderBy(aliasGrowthRate.desc())
                .limit(5)
                .fetch()).orElse(List.of()));

        queryResult.setStockBottomFiveGrowthRate(Optional.ofNullable(queryFactory
                .select(Projections.fields(StockInfoRes.class,
                        stockInfo.stockCode
                        , stockInfo.stockName
                        , stockInfo.startingPrice
                        , stockInfo.currentPrice
                        , stockInfo.hits
                        , new CaseBuilder().when(stockInfo.buyingCount.lt(stockInfo.sellingCount)).then(stockInfo.buyingCount).otherwise(stockInfo.sellingCount).as("tradingVolume")
                        , MathExpressions.round(stockInfo.currentPrice.doubleValue().subtract(stockInfo.currentPrice.doubleValue()).divide(stockInfo.startingPrice.doubleValue()).multiply(100.00), 2).as("growthRate")
                ))
                .from(stockInfo)
                .where(eqNationOrDefault(nation))
                .orderBy(aliasGrowthRate.asc())
                .limit(5)
                .fetch()).orElse(List.of()));

        assertThat(queryResult.getStockTopFiveHits()).isNotEmpty();
        assertThat(queryResult.getStockTopFiveHits().size()).isEqualTo(5);

        assertThat(queryResult.getStockTopFiveTradingVolume()).isNotEmpty();
        assertThat(queryResult.getStockTopFiveTradingVolume().size()).isEqualTo(5);

        assertThat(queryResult.getStockTopFiveGrowthRate()).isNotEmpty();
        assertThat(queryResult.getStockTopFiveGrowthRate().size()).isEqualTo(5);

        assertThat(queryResult.getStockBottomFiveGrowthRate()).isNotEmpty();
        assertThat(queryResult.getStockBottomFiveGrowthRate().size()).isEqualTo(5);
    }

    @Test
    public void selectStockInfo_findDetailTopHits(){
        List<StockInfo> stockInfoList = Optional.of(queryFactory.selectFrom(stockInfo).where(eqNationOrDefault(nation)).fetch()).orElse(List.of());
        stockInfoList.forEach(StockInfo::reRanking);
        Pageable pageable = PageRequest.of(0,20);
        List<Long> hitsList = Optional.ofNullable(queryFactory
                        .selectFrom(stockInfo)
                        .where(eqNationOrDefault(nation))
                        .orderBy(stockInfo.hits.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch())
                .orElse(List.of())
                .stream()
                .mapToLong(StockInfo::getHits)
                .boxed()
                .collect(Collectors.toList());

        Page<Long> page = PageableExecutionUtils.getPage(hitsList,pageable,() -> {return 100;});

        List<Long> list = page.get().collect(Collectors.toList());
        List<Long> tmplist = new ArrayList<>(list);

        Collections.sort(list,Collections.reverseOrder());

        assertThat(page.getTotalElements()).isEqualTo(100);
        assertThat(page.getTotalPages()).isEqualTo(5);
        assertThat(page.getSize()).isEqualTo(20);
        assertThat(tmplist).isEqualTo(list);
    }

    @Test
    public void selectStockInfo_findDetailTopTradingVolume(){
        List<StockInfo> stockInfoList = Optional.of(queryFactory.selectFrom(stockInfo).where(eqNationOrDefault(nation)).fetch()).orElse(List.of());
        stockInfoList.forEach(StockInfo::reRanking);
        Pageable pageable = PageRequest.of(0,20);
        StringPath aliasTradingVolume = Expressions.stringPath("tradingVolume");
        List<Long> tradingVolumeList = Optional.ofNullable(queryFactory
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
                        .where(eqNationOrDefault(nation))
                        .orderBy(aliasTradingVolume.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch())
                .orElse(List.of())
                .stream()
                .mapToLong(StockInfoRes::getTradingVolume)
                .boxed()
                .collect(Collectors.toList());

        Page<Long> page = PageableExecutionUtils.getPage(tradingVolumeList,pageable,() -> {return 100;});

        List<Long> list = page.get().collect(Collectors.toList());
        List<Long> tmplist = new ArrayList<>(list);

        Collections.sort(list,Collections.reverseOrder());

        assertThat(page.getTotalElements()).isEqualTo(100);
        assertThat(page.getTotalPages()).isEqualTo(5);
        assertThat(page.getSize()).isEqualTo(20);
        assertThat(tmplist).isEqualTo(list);
    }

    @Test
    public void selectStockInfo_findDetailTopGrowThRate(){
        List<StockInfo> stockInfoList = Optional.of(queryFactory.selectFrom(stockInfo).where(eqNationOrDefault(nation)).fetch()).orElse(List.of());
        stockInfoList.forEach(StockInfo::reRanking);
        Pageable pageable = PageRequest.of(0,20);
        StringPath aliasGrowthRate = Expressions.stringPath("growthRate");
        List<Double> growthRateList = Optional.ofNullable(queryFactory
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
                        .where(eqNationOrDefault(nation))
                        .orderBy(aliasGrowthRate.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch())
                .orElse(List.of())
                .stream()
                .mapToDouble(StockInfoRes::getGrowthRate)
                .boxed()
                .collect(Collectors.toList());

        Page<Double> page = PageableExecutionUtils.getPage(growthRateList,pageable,() -> {return 100;});

        List<Double> list = page.get().collect(Collectors.toList());
        List<Double> tmplist = new ArrayList<>(list);

        Collections.sort(list,Collections.reverseOrder());

        assertThat(page.getTotalElements()).isEqualTo(100);
        assertThat(page.getTotalPages()).isEqualTo(5);
        assertThat(page.getSize()).isEqualTo(20);
        assertThat(tmplist).isEqualTo(list);
    }

    @Test
    public void selectStockInfo_findDetailBottomGrowThRate(){
        List<StockInfo> stockInfoList = Optional.of(queryFactory.selectFrom(stockInfo).where(eqNationOrDefault(nation)).fetch()).orElse(List.of());
        stockInfoList.forEach(StockInfo::reRanking);
        Pageable pageable = PageRequest.of(0,20);
        StringPath aliasGrowthRate = Expressions.stringPath("growthRate");
        List<Double> growthRateList = Optional.ofNullable(queryFactory
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
                        .where(eqNationOrDefault(nation))
                        .orderBy(aliasGrowthRate.asc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch())
                .orElse(List.of())
                .stream()
                .mapToDouble(StockInfoRes::getGrowthRate)
                .boxed()
                .collect(Collectors.toList());

        Page<Double> page = PageableExecutionUtils.getPage(growthRateList,pageable,() -> {return 100;});

        List<Double> list = page.get().collect(Collectors.toList());
        List<Double> tmplist = new ArrayList<>(list);

        Collections.sort(list,Collections.reverseOrder());

        assertThat(page.getTotalElements()).isEqualTo(100);
        assertThat(page.getTotalPages()).isEqualTo(5);
        assertThat(page.getSize()).isEqualTo(20);
        assertThat(tmplist).isEqualTo(list);
    }

}