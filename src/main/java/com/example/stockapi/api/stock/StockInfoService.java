package com.example.stockapi.api.stock;

import com.example.stockapi.api.exception.QueryNoExistException;
import com.example.stockapi.api.repository.StockInfoRepository;
import com.example.stockapi.api.repository.domain.StockInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        return stockInfoRepository.findAll()
                .filter(item->item.size()>0)
                .orElseThrow(() -> new QueryNoExistException("cannot found stocks"))
                .stream()
                .map(StockInfo::getStockInfoRes)
                .toList();
    }
    /**
     * 순위가 변동하는 메서드
     * */
    @Transactional
    public long reRanking() {
        return stockInfoRepository.findAll()
                .filter(item->item.size()>0)
                .orElseThrow(()-> new QueryNoExistException("cannot found stocks"))
                .stream()
                .mapToLong(StockInfo::reRanking)
                .sum();
    }

    /**
     * 많이 본, 많이 상승, 많이 하락, 많이 거래 된 4개의 분류의 tob5 조회
     */
    @Transactional(readOnly = true)
    public StockTopFiveAllRes<List<StockInfoRes>> stockTopFiveAll(){
        return stockInfoRepository.findTopFiveAll();
    }
    @Transactional(readOnly = true)
    public List<StockInfoRes> stockDetailTopHits(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset,limit);
        return stockInfoRepository.findDetailTopHits(pageable)
                .filter(item->item.size()>0)
                .orElseThrow(()->new QueryNoExistException("cannot found StockInfo"));
    }

    @Transactional(readOnly = true)
    public List<StockInfoRes> stockDetailTopTradingVolume(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset,limit);
        return stockInfoRepository.findDetailTopTradingVolume(pageable)
                .filter(item->item.size()>0)
                .orElseThrow(()->new QueryNoExistException("cannot found StockInfo"));
    }

    @Transactional(readOnly = true)
    public List<StockInfoRes> stockDetailTopGrowthRate(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset,limit);
        return stockInfoRepository.findDetailTopGrowthRate(pageable)
                .filter(item->item.size()>0)
                .orElseThrow(()->new QueryNoExistException("cannot found StockInfo"));
    }

    @Transactional(readOnly = true)
    public List<StockInfoRes> stockDetailBottomGrowthRate(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset,limit);
        return stockInfoRepository.findDetailBottomGrowthRate(pageable)
                .filter(item->item.size()>0)
                .orElseThrow(()->new QueryNoExistException("cannot found StockInfo"));
    }
}
