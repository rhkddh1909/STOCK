package com.example.stockapi.api.marketinfo;

import com.example.stockapi.api.exception.QueryNoExistException;
import com.example.stockapi.api.repository.MarketInfoRepository;
import com.example.stockapi.api.repository.StockInfoHistorySaveRepository;
import com.example.stockapi.api.repository.StockInfoRepository;
import com.example.stockapi.api.repository.domain.StockInfoHistory;
import com.example.stockapi.api.stockhistory.StockInfoHistoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.stockapi.api.util.CUSTOM_CODE.RSEULT.CLOSE;
import static com.example.stockapi.api.util.CUSTOM_CODE.RSEULT.OPEN;

@Service
@RequiredArgsConstructor
public class MarketInfoService {
    private final MarketInfoRepository marketInfoRepository;
    private final StockInfoRepository stockInfoRepository;
    private final StockInfoHistorySaveRepository stockInfoHistorySaveRepository;

    @Transactional(readOnly = true)
    public List<MarketInfoRes> getMarketList() {
        return marketInfoRepository.findAll()
                .filter(item->item.size() > 0)
                .orElseThrow(()->new QueryNoExistException("cannot found marketInfo"));
    }
    
    @Transactional(readOnly = true)
    public MarketInfoRes getMarketInfoByCode(String marketCode) {
        return marketInfoRepository.findByMarketCode(marketCode)
                .orElseThrow(()->new QueryNoExistException("cannot found marketInfo"));
    }

    @Transactional(readOnly = true)
    public List<MarketInfoRes> getMarketInfoByNation(String marketNation) {
        return marketInfoRepository.findByMarketNation(marketNation)
                .filter(item->item.size() > 0)
                .orElseThrow(()->new QueryNoExistException("cannot found marketInfo"));
    }
    @Transactional
    public Long openMarket(String nation) {
        return marketInfoRepository.bulkUpdateMarketOpen(nation, OPEN.CODE())
                .filter(item->item != 0L).orElseThrow(()->new QueryNoExistException("cannot found update info"));
    }

    @Transactional
    public Long closeMarket(String nation) {
        Long updateCount = marketInfoRepository.bulkUpdateMarketOpen(nation, CLOSE.CODE())
                .filter(item -> item != 0L).orElseThrow(() -> new QueryNoExistException("cannot found update info"));

        List<StockInfoHistory> stockInfoHistoryList = stockInfoRepository.selectStockInfoHistorys(nation)
                .filter(item -> item.size() != 0)
                .orElseThrow(() -> new QueryNoExistException("cannot found marketInfo"))
                .stream()
                .map(StockInfoHistoryDto::getStockInfoHistory)
                .toList();

        Optional.of(stockInfoHistorySaveRepository.saveAll(stockInfoHistoryList))
                .filter(item->item.size() != 0)
                .orElseThrow(()->new QueryNoExistException("cannot found insert info"));
        return updateCount;
    }
}
