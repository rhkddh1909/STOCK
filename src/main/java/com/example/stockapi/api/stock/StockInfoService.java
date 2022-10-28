package com.example.stockapi.api.stock;

import com.example.stockapi.api.repository.StockInfoRepository;
import com.example.stockapi.api.repository.domain.StockInfo;
import com.example.stockapi.api.exception.StockNoExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StockInfoService {
    private final StockInfoRepository stockInfoRepository;

    /**
     * 주식 종목 기본 정보 조회
     * */
    public List<StockInfoRes> stockInfos() {
        return stockInfoRepository.findAll().orElseThrow(() -> new StockNoExistException("cannot found stocks"))
                .stream()
                .map(StockInfo::getStockInfoRes)
                .toList();
    }
}
