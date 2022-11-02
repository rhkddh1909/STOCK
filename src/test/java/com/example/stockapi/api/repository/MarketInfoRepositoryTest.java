package com.example.stockapi.api.repository;

import com.example.stockapi.api.marketinfo.MarketInfoRes;
import com.example.stockapi.api.repository.domain.MarketInfo;
import com.example.stockapi.config.QuerydslTestConfiguration;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

import static com.example.stockapi.api.repository.domain.QMarketInfo.marketInfo;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QuerydslTestConfiguration.class)
public class MarketInfoRepositoryTest {
    @Autowired
    private JPAQueryFactory queryFactory;

    @Test
    @Transactional(readOnly = true)
    public void selectMarketInfoList_findAll(){
        List<MarketInfoRes> marketInfoListTest = Optional.of(queryFactory
                .select(Projections.fields(MarketInfoRes.class,
                        marketInfo.marketName
                        , marketInfo.marketOpenYn
                        , marketInfo.marketNation
                ))
                .from(marketInfo)
                .fetch()).orElse(List.of());

        assertThat(marketInfoListTest.size()).isEqualTo(2);
        assertThat(marketInfoListTest.get(0)).isEqualTo(new MarketInfoRes("KOSPI","N","KOR"));
        assertThat(marketInfoListTest.get(1)).isEqualTo(new MarketInfoRes("KOSDAQ","N","KOR"));
    }
    @Test
    @Transactional(readOnly = true)
    public void selectMarketInfo_findByMarketCode(){
        String marketCode = "0001";
        MarketInfoRes marketInfoRes = Optional.ofNullable(queryFactory
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
                .findFirst()
                .get();

        assertThat(marketInfoRes).isEqualTo(new MarketInfoRes("KOSPI","N","KOR"));
    }

    @Test
    @Transactional(readOnly = true)
    public void selectMarketInfo_findByMarketNation(){
        String marketNation = "KOR";
        List<MarketInfoRes> marketInfoResList = Optional.ofNullable(queryFactory
                        .select(Projections.fields(MarketInfoRes.class,
                                marketInfo.marketName
                                ,marketInfo.marketOpenYn
                                ,marketInfo.marketNation
                        ))
                        .from(marketInfo)
                        .where(marketInfo.marketNation.eq(marketNation))
                        .fetch()).orElse(List.of());

        assertThat(marketInfoResList.get(0)).isEqualTo(new MarketInfoRes("KOSPI","N","KOR"));
        assertThat(marketInfoResList.get(1)).isEqualTo(new MarketInfoRes("KOSDAQ","N","KOR"));
    }
}
