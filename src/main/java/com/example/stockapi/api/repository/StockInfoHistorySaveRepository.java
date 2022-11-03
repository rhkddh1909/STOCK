package com.example.stockapi.api.repository;

import com.example.stockapi.api.repository.domain.StockInfoHistory;
import com.example.stockapi.api.repository.domain.StockInfoHistoryID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockInfoHistorySaveRepository extends JpaRepository<StockInfoHistory, StockInfoHistoryID> {

}
