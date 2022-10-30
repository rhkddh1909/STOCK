package com.example.stockapi.api.stock;

import com.example.stockapi.api.exception.StockNoExistException;
import com.example.stockapi.api.repository.StockInfoRepository;
import com.example.stockapi.api.repository.domain.StockInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockInfoService {
    private final StockInfoRepository stockInfoRepository;

    /**
     * 주식 종목 기본 정보 조회
     * */
    @Transactional(readOnly=true)
    public List<StockInfoRes> stockInfos() {
        return stockInfoRepository.findAll().orElseThrow(() -> new StockNoExistException("cannot found stocks"))
                .stream()
                .map(StockInfo::getStockInfoRes)
                .toList();
    }
    /**
     * hits update
     * */
    @Transactional
    public long reRanking() {
        return stockInfoRepository.findAll().orElseThrow(()-> new StockNoExistException("cannot found stocks"))
                .stream()
                .toList()
                .stream()
                .mapToLong(item->{
                    item.updateCurrPrice();
                    return 1L;
                })
                .sum();
    }

    /**
     * 많이 본, 많이 상승, 많이 하락, 많이 거래 된 4개의 분류의 tob5 조회
     */
    @Transactional(readOnly = true)
    public StockTopFiveAllRes<List<StockInfoRes>> stockTopFiveAll(){
        return stockInfoRepository.findTopFiveAll().orElseThrow(()->new StockNoExistException("cannot found StockInfo"));
    }
}
