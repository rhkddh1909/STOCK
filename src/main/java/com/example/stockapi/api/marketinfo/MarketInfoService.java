package com.example.stockapi.api.marketinfo;

import com.example.stockapi.api.exception.QueryNoExistException;
import com.example.stockapi.api.repository.MarketInfoRepository;
import com.example.stockapi.api.repository.domain.MarketInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketInfoService {
    private final MarketInfoRepository marketInfoRepository;


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
}
