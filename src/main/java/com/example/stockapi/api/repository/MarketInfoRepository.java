package com.example.stockapi.api.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MarketInfoRepository {
    private final JPAQueryFactory queryFactory;

}
