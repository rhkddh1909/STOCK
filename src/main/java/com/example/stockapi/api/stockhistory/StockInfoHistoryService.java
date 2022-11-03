package com.example.stockapi.api.stockhistory;

import com.example.stockapi.api.exception.QueryNoExistException;
import com.example.stockapi.api.repository.StockInfoHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockInfoHistoryService {
    private final StockInfoHistoryRepository stockInfoHistoryRepository;
    @Transactional
    public List<StockInfoHistoryDto> stockInfoHistories(String nation, String stockCode) {
        return stockInfoHistoryRepository.findByNationAndStockCode(nation, stockCode).orElseThrow(()->new QueryNoExistException("cannot found stock history"));
    }
    @Transactional
    public List<StockInfoHistoryDto> stockInfoHistoryAnalysis(String nation, String stockCode,Long analsisTerm) {
        return stockInfoHistoryRepository.findStockInfoHistoryGroupData(nation,stockCode,analsisTerm).orElseThrow(()->new QueryNoExistException("cannot found stock history"));
    }
}
