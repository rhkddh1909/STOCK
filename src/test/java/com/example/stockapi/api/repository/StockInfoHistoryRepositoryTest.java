package com.example.stockapi.api.repository;

import com.example.stockapi.api.repository.domain.MarketInfo;
import com.example.stockapi.api.repository.domain.StockInfoHistory;
import com.example.stockapi.api.repository.domain.StockInfoHistoryID;
import com.example.stockapi.api.stockhistory.StockInfoHistoryDto;
import com.example.stockapi.config.QuerydslTestConfiguration;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

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

    private String nation = "KOR";
    private String stockCode = "000660";
    private final Long analsisTerm = 5L;
    private BooleanExpression eqNationOrDefault(String nation){
        return Optional.ofNullable(stockInfoHistory.marketNation.eq(nation))
                .orElse(stockInfoHistory.marketNation.eq("KOR"));
    }

    private BooleanExpression eqStockCode(String stockCode){
        return Optional.ofNullable(stockInfoHistory.marketNation.eq(stockCode))
                .orElse(null);
    }

    @Test
    public void stockInfohistories_findByNationAndStockCode(){
        StockInfoHistory stockInfoHistory0 = StockInfoHistory.builder()
                .stockInfoHistoryID(StockInfoHistoryID.builder().historyId(1L).stockCode("000660").build())
                .stockName("SK하이닉스")
                .marketCode("0001")
                .marketName("KOSDAQ")
                .startingPrice(95700L)
                .endingPrice(95700L)
                .buyingCount(0L)
                .sellingCount(0L)
                .tradingVolume(0L)
                .growthRate(0.0)
                .hits(0L)
                .marketNation("KOR")
                .build();

        StockInfoHistory stockInfoHistory1 = StockInfoHistory.builder()
                .stockInfoHistoryID(StockInfoHistoryID.builder().historyId(2L).stockCode("000660").build())
                .stockName("SK하이닉스")
                .marketCode("0001")
                .marketName("KOSDAQ")
                .startingPrice(95700L)
                .endingPrice(95700L)
                .buyingCount(0L)
                .sellingCount(0L)
                .tradingVolume(0L)
                .growthRate(0.0)
                .hits(0L)
                .marketNation("KOR")
                .build();

        List<StockInfoHistory> list = stockInfoHistorySaveRepository.saveAll(List.of(stockInfoHistory0,stockInfoHistory1));

        List<StockInfoHistoryDto> stockInfoHistoryDtos = queryFactory
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
                .fetch();

        assertThat(stockInfoHistoryDtos).hasSize(2);
        assertThat(stockInfoHistoryDtos.get(0).getStockCode()).isEqualTo(stockInfoHistory0.getStockInfoHistoryID().getStockCode());
        assertThat(stockInfoHistoryDtos.get(1).getStockCode()).isEqualTo(stockInfoHistory1.getStockInfoHistoryID().getStockCode());
    }
}