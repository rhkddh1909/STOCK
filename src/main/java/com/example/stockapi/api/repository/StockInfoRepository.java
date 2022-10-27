package com.example.stockapi.api.repository;

import com.example.stockapi.api.repository.domain.StockInfo;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface StockInfoRepository extends Repository<StockInfo,Long> {
    List<Optional<StockInfo>> findAll();
}
