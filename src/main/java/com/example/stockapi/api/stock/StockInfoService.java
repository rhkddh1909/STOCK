package com.example.stockapi.api.stock;

import com.example.stockapi.api.exception.MarketException;
import com.example.stockapi.api.exception.QueryNoExistException;
import com.example.stockapi.api.marketinfo.MarketInfoRes;
import com.example.stockapi.api.repository.MarketInfoRepository;
import com.example.stockapi.api.repository.StockInfoRepository;
import com.example.stockapi.api.repository.domain.StockInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.stockapi.api.util.CUSTOM_CODE.RSEULT.CLOSE;
import static com.example.stockapi.api.util.CUSTOM_CODE.RSEULT.OPEN;

@Service
@RequiredArgsConstructor
public class StockInfoService {
    private final StockInfoRepository stockInfoRepository;
    private final MarketInfoRepository MarketInfoRepository;

    /**
     * 주식 종목 기본 정보 조회
     * */
    @Transactional(readOnly=true)
    public List<StockInfoRes> stockInfos(String nation, String marketCode) {
        return stockInfoRepository.findByNation(nation, marketCode)
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
    public long reRanking(String nation, String marketCode) {
        MarketInfoRepository.findByMarketNation(nation)
                .filter(item->item.size() !=0)
                .orElseThrow(()->new MarketException(Optional.ofNullable(nation).orElse("KOR")+" Market is not open"))
                .stream()
                .forEach(marketInfo->Optional.ofNullable(marketInfo.getMarketOpenYn())
                        .filter(yn->yn.equals(OPEN.CODE()))
                        .orElseThrow(()->new MarketException("시장이 열리지 않았습니다."))
                );

        return stockInfoRepository.findByNation(nation, marketCode)
                .filter(item->item.size()>0)
                .orElseThrow(()-> new QueryNoExistException("cannot found stocks to reRanking"))
                .stream()
                .mapToLong(StockInfo::reRanking)
                .sum();
    }

    /**
     * 많이 본, 많이 상승, 많이 하락, 많이 거래 된 4개의 분류의 tob5 조회
     */
    @Transactional(readOnly = true)
    public StockTopFiveAllRes<List<StockInfoRes>> stockTopFiveAll(String nation, String marketCode){
        return stockInfoRepository.findTopFiveAll(nation, marketCode);
    }
    @Transactional(readOnly = true)
    public List<StockInfoRes> stockDetailTopHits(int offset, int limit, String nation, String marketCode) {
        Pageable pageable = PageRequest.of(offset,limit);
        return stockInfoRepository.findDetailTopHits(pageable,nation, marketCode)
                .filter(item->item.size()>0)
                .orElseThrow(()->new QueryNoExistException("cannot found StockInfo"));
    }

    @Transactional(readOnly = true)
    public List<StockInfoRes> stockDetailTopTradingVolume(int offset, int limit ,String nation, String marketCode) {
        Pageable pageable = PageRequest.of(offset,limit);
        return stockInfoRepository.findDetailTopTradingVolume(pageable, nation, marketCode)
                .filter(item->item.size()>0)
                .orElseThrow(()->new QueryNoExistException("cannot found StockInfo"));
    }

    @Transactional(readOnly = true)
    public List<StockInfoRes> stockDetailTopGrowthRate(int offset, int limit, String nation, String marketCode) {
        Pageable pageable = PageRequest.of(offset,limit);
        return stockInfoRepository.findDetailTopGrowthRate(pageable, nation, marketCode)
                .filter(item->item.size()>0)
                .orElseThrow(()->new QueryNoExistException("cannot found StockInfo"));
    }

    @Transactional(readOnly = true)
    public List<StockInfoRes> stockDetailBottomGrowthRate(int offset, int limit, String nation, String marketCode) {
        Pageable pageable = PageRequest.of(offset,limit);
        return stockInfoRepository.findDetailBottomGrowthRate(pageable, nation, marketCode)
                .filter(item->item.size()>0)
                .orElseThrow(()->new QueryNoExistException("cannot found StockInfo"));
    }
}
