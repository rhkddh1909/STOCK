package com.example.stockapi.api.repository;

import com.example.stockapi.api.repository.domain.MarketInfo;
import com.example.stockapi.api.repository.domain.QMarketInfo;
import com.example.stockapi.config.QuerydslTestConfiguration;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

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
    public void selectMarketInfoList_findAll(){
        List<MarketInfo> marketInfoListTest = Optional.of(queryFactory
                .selectFrom(marketInfo)
                .fetch()).orElse(List.of());

        assertThat(marketInfoListTest.size()).isEqualTo(2);
        assertThat(marketInfoListTest.get(0)).isEqualTo(new MarketInfo("0001","KOSPI","N","KOR",0L));
        assertThat(marketInfoListTest.get(1)).isEqualTo(new MarketInfo("0002","KOSDAQ","N","KOR",0L));
    }
    @Test
    public void selectMarketInfo_findByMarketCode(){
        String marketCode = "0001";
        MarketInfo marketInfoTest = Optional.ofNullable(queryFactory
                .selectFrom(marketInfo)
                .where(marketInfo.marketCode.eq(marketCode))
                .fetch())
                .orElse(List.of())
                .stream()
                .findFirst()
                .get();

        assertThat(marketInfoTest).isEqualTo(new MarketInfo("0001","KOSPI","N","KOR",0L));
    }
}
