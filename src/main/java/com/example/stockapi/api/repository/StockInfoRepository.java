package com.example.stockapi.api.repository;

import com.example.stockapi.api.repository.domain.StockInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
}
