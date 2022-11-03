package com.example.stockapi.api.repository;

import com.example.stockapi.api.marketinfo.MarketInfoRes;
import com.example.stockapi.api.repository.domain.MarketInfo;
import com.example.stockapi.config.QuerydslTestConfiguration;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.stockapi.api.repository.domain.QMarketInfo.marketInfo;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QuerydslTestConfiguration.class)
public class MarketInfoRepositoryTest {
    @Autowired
    private JPAQueryFactory queryFactory;

    /**nation을 입력받고 nation값을 체크하여 동적 쿼리 추가**/
    private BooleanExpression eqNationOrDefault(String nation){
        return Optional.ofNullable(nation)
                .map(item->marketInfo.marketNation.eq(nation.toUpperCase()))
                .orElse(marketInfo.marketNation.eq("KOR"));
    }

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

    @Test
    @Transactional
    public void updateMarketInfo_startingMarket(){
        String nation = "KOR";
        String yn = "Y";
        assertThat(queryFactory
                .update(marketInfo)
                .set(marketInfo.marketOpenYn,yn)
                .set(marketInfo.marketSequence, marketInfo.marketSequence.add(yn.equals("Y") ? 1L : 0L))
                .where(
                        eqNationOrDefault(nation)
                        ,marketInfo.marketOpenYn.ne(yn)
                )
                .execute()).isEqualTo(2L);

        MarketInfo marketInfoRes = queryFactory.selectFrom(marketInfo).where(eqNationOrDefault(nation),marketInfo.marketCode.eq("0001")).fetch().get(0);

        assertThat(marketInfoRes.getMarketSequence()).isEqualTo(1L);
    }
}
