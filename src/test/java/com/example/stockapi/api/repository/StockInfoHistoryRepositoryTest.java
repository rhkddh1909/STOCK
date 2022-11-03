package com.example.stockapi.api.repository;

import com.example.stockapi.api.exception.QueryNoExistException;
import com.example.stockapi.api.repository.domain.QMarketInfo;
import com.example.stockapi.api.repository.domain.StockInfoHistory;
import com.example.stockapi.api.stockhistory.StockInfoHistoryDto;
import com.example.stockapi.config.QuerydslTestConfiguration;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static com.example.stockapi.api.repository.domain.QMarketInfo.marketInfo;
import static com.example.stockapi.api.repository.domain.QStockInfoHistory.stockInfoHistory;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QuerydslTestConfiguration.class)
class StockInfoHistoryRepositoryTest {
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    StockInfoHistorySaveRepository stockInfoHistorySaveRepository;
    private final StringPath aliasGrowthRate = Expressions.stringPath("growthRate");
    private String nation = null;
    private String stockCode = null;
    private final Long analsisTerm = 1L;

    private BooleanExpression eqMarketNationOrDefault(String nation){
        return Optional.ofNullable(marketInfo.marketNation.eq(nation))
                .orElse(marketInfo.marketNation.eq("KOR"));
    }
    private BooleanExpression eqNationOrDefault(String nation){
        return Optional.ofNullable(stockInfoHistory.marketNation.eq(nation))
                .orElse(stockInfoHistory.marketNation.eq("KOR"));
    }

    private BooleanExpression eqStockCode(String stockCode){
        return Optional.ofNullable(stockInfoHistory.stockCode.eq(stockCode))
                .orElse(null);
    }

    private NumberExpression<Long> getStartingPrice(Long sequence){
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

    @Test
    public void stockInfohistories_findByNationAndStockCode(){
        StockInfoHistory stockInfoHistory0= new StockInfoHistory(1L,"000660","SK하이닉스","0001","KOSDAQ",95700L,95700L,0L,0L,0L,0.0,0L,"KOR");

        StockInfoHistory stockInfoHistory1 = new StockInfoHistory(2L,"000660","SK하이닉스","0001","KOSDAQ",95700L,95700L,0L,0L,0L,0.0,0L,"KOR");

        List<StockInfoHistory> list = stockInfoHistorySaveRepository.saveAll(List.of(stockInfoHistory0,stockInfoHistory1));

        List<StockInfoHistoryDto> stockInfoHistoryDtos = queryFactory
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
                .fetch();

        assertThat(stockInfoHistoryDtos).hasSize(2);
        assertThat(stockInfoHistoryDtos.get(0).getStockCode()).isEqualTo(stockInfoHistory0.getStockCode());
        assertThat(stockInfoHistoryDtos.get(1).getStockCode()).isEqualTo(stockInfoHistory1.getStockCode());
    }

    @Test
    public void stockInfoHistoryAnalysis_findStockInfoHistoryGroupData(){
        StockInfoHistory stockInfoHistory0 = new StockInfoHistory(1L,"000660","SK하이닉스","0001","KOSDAQ",95700L,95700L,0L,0L,0L,0.0,0L,"KOR");

        StockInfoHistory stockInfoHistory1 = new StockInfoHistory(2L,"000660","SK하이닉스","0001","KOSDAQ",95700L,95700L,0L,0L,0L,0.0,0L,"KOR");

        List<StockInfoHistory> list = stockInfoHistorySaveRepository.saveAll(List.of(stockInfoHistory0,stockInfoHistory1));

        Long sequence = queryFactory
                .selectFrom(marketInfo)
                .where(eqMarketNationOrDefault(nation))
                .limit(1)
                .fetch()
                .stream()
                .mapToLong(item->item.getMarketSequence())
                .filter(item->item!=0L)
                .findFirst().orElse(2L);

        List<StockInfoHistoryDto> stockInfoHistoryAnalysis = queryFactory
                .select(Projections.fields(StockInfoHistoryDto.class
                        ,stockInfoHistory.stockCode
                        ,stockInfoHistory.stockName
                        ,stockInfoHistory.marketName
                        ,getStartingPrice(sequence).sum().as("startingPrice")
                        ,getEndingPrice(sequence).sum().as("endingPrice")
                        ,stockInfoHistory.buyingCount.sum().as("buyingCount")
                        ,stockInfoHistory.sellingCount.sum().as("sellingCount")
                        ,stockInfoHistory.tradingVolume.sum().as("tradingVolume")
                        ,stockInfoHistory.hits.sum().as("hits")
                        ,stockInfoHistory.marketNation
                        ,MathExpressions.round(
                                getEndingPrice(sequence).sum().doubleValue()
                                        .subtract(getStartingPrice(sequence).sum().doubleValue())
                                        .divide(getStartingPrice(sequence).sum().doubleValue())
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
                .fetch();

        assertThat(stockInfoHistoryAnalysis).hasSize(1);
    }
}