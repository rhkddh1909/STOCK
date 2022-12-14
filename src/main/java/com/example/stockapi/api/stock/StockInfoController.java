package com.example.stockapi.api.stock;

import com.example.stockapi.api.exception.QueryNoExistException;
import com.example.stockapi.api.util.BaseDto;
import com.example.stockapi.api.util.CUSTOM_CODE;
import com.example.stockapi.api.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.example.stockapi.api.util.CUSTOM_CODE.RSEULT.SUCCESS;

@RestController
@RequiredArgsConstructor
public class StockInfoController {
    private final StockInfoService stockInfoService;


    @GetMapping("stockInfos")
    public BaseDto<List<StockInfoRes>> getStockInfoList(String nation, String marketCode) {
        try {
            List<StockInfoRes> stockInfoRes = stockInfoService.stockInfos(nation, marketCode);

            return BaseDto.<List<StockInfoRes>>builder()
                    .status(SUCCESS.CODE())
                    .rsltMsg("")
                    .rsltData(stockInfoRes)
                    .build();
        }
        catch(Exception e){
            return Util.getErrorBody(e.getMessage(), new ArrayList<>());
        }
    }
    @GetMapping("reRanking")
    public BaseDto<Object> reRanking(String nation, String marketCode) {
        try{
            Long reRankCount = stockInfoService.reRanking(nation,marketCode);

            return BaseDto.builder()
                    .status(SUCCESS.CODE())
                    .rsltMsg(reRankCount+"건의 데이터가 변동되었습니다.")
                    .build();
        }
        catch(Exception e){
            return Util.getErrorBody(e.getMessage(), new Object());
        }
    }
    @GetMapping("stockTopFiveAll")
    public BaseDto<StockTopFiveAllRes<List<StockInfoRes>>> stockTopFiveAll(String nation, String marketCode) {
        try{
            StockTopFiveAllRes<List<StockInfoRes>> stockTopFiveAllRes = stockInfoService.stockTopFiveAll(nation, marketCode);

            return BaseDto.<StockTopFiveAllRes<List<StockInfoRes>>>builder()
                    .status(SUCCESS.CODE())
                    .rsltMsg("")
                    .rsltData(stockTopFiveAllRes)
                    .build();
        }
        catch(Exception e){
            return Util.getErrorBody(e.getMessage(), new StockTopFiveAllRes<>());
        }
    }
    @GetMapping("stockDetailTopHits")
    public BaseDto<List<StockInfoRes>> stockDetailTopHits(@RequestParam int pageNum , @RequestParam int pageSize, String nation, String marketCode) {
        try{
            List<StockInfoRes> stockDetailTopHitsRes = stockInfoService.stockDetailTopHits(pageNum, pageSize, nation, marketCode);

            return BaseDto.<List<StockInfoRes>>builder()
                    .status(SUCCESS.CODE())
                    .rsltMsg("")
                    .rsltData(stockDetailTopHitsRes)
                    .build();
        }
        catch(Exception e){
            return Util.getErrorBody(e.getMessage(), new ArrayList<>());
        }
    }

    @GetMapping("stockDetailTopTradingVolume")
    public BaseDto<List<StockInfoRes>> stockDetailTopTradingVolume(@RequestParam int pageNum , @RequestParam int pageSize, String nation, String marketCode) {
        try{
            List<StockInfoRes> stockDetailTopHitsRes = stockInfoService.stockDetailTopTradingVolume(pageNum, pageSize, nation, marketCode);

            return BaseDto.<List<StockInfoRes>>builder()
                    .status(SUCCESS.CODE())
                    .rsltMsg("")
                    .rsltData(stockDetailTopHitsRes)
                    .build();
        }
        catch(QueryNoExistException e){
            return Util.getErrorBody(e.getMessage(), new ArrayList<>());
        }
    }

    @GetMapping("stockDetailTopGrowthRate")
    public BaseDto<List<StockInfoRes>> stockDetailTopGrowthRate(@RequestParam int pageNum , @RequestParam int pageSize, String nation, String marketCode) {
        try{
            List<StockInfoRes> stockDetailTopHitsRes = stockInfoService.stockDetailTopGrowthRate(pageNum, pageSize, nation, marketCode);

            return BaseDto.<List<StockInfoRes>>builder()
                    .status(SUCCESS.CODE())
                    .rsltMsg("")
                    .rsltData(stockDetailTopHitsRes)
                    .build();
        }
        catch(QueryNoExistException e){
            return Util.getErrorBody(e.getMessage(), new ArrayList<>());
        }
    }

    @GetMapping("stockDetailBottomGrowthRate")
    public BaseDto<List<StockInfoRes>> stockDetailBottomGrowthRate(@RequestParam int pageNum , @RequestParam int pageSize, String nation, String marketCode) {
        try{
            List<StockInfoRes> stockDetailTopHitsRes = stockInfoService.stockDetailBottomGrowthRate(pageNum, pageSize, nation, marketCode);

            return BaseDto.<List<StockInfoRes>>builder()
                    .status(SUCCESS.CODE())
                    .rsltMsg("")
                    .rsltData(stockDetailTopHitsRes)
                    .build();
        }
        catch(QueryNoExistException e){
            return Util.getErrorBody(e.getMessage(), new ArrayList<>());
        }
    }
}
