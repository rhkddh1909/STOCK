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

@RestController
@RequiredArgsConstructor
public class StockInfoController {
    private final StockInfoService stockInfoService;


    @GetMapping("stockInfos")
    public BaseDto<List<StockInfoRes>> getStockInfoList(@RequestParam String nation) {
        try {
            List<StockInfoRes> stockInfoRes = stockInfoService.stockInfos(nation);

            return BaseDto.<List<StockInfoRes>>builder()
                    .status(CUSTOM_CODE.RSEULT.SUCCESS.STATUS())
                    .rsltMsg("")
                    .rsltData(stockInfoRes)
                    .build();
        }
        catch(QueryNoExistException e){
            return Util.getErrorBody(e.getMessage(), new ArrayList<StockInfoRes>());
        }
    }
    @GetMapping("reRanking")
    public BaseDto<Object> reRanking(@RequestParam String nation) {
        try{
            Long reRankCount = stockInfoService.reRanking(nation);

            return BaseDto.builder()
                    .status(CUSTOM_CODE.RSEULT.SUCCESS.STATUS())
                    .rsltMsg(reRankCount+"건의 데이터가 변동되었습니다.")
                    .build();
        }
        catch(QueryNoExistException e){
            return Util.getErrorBody(e.getMessage(), new Object());
        }
    }
    @GetMapping("stockTopFiveAll")
    public BaseDto<StockTopFiveAllRes<List<StockInfoRes>>> stockTopFiveAll(@RequestParam String nation) {
        try{
            StockTopFiveAllRes<List<StockInfoRes>> stockTopFiveAllRes = stockInfoService.stockTopFiveAll(nation);

            return BaseDto.<StockTopFiveAllRes<List<StockInfoRes>>>builder()
                    .status(CUSTOM_CODE.RSEULT.SUCCESS.STATUS())
                    .rsltMsg("")
                    .rsltData(stockTopFiveAllRes)
                    .build();
        }
        catch(QueryNoExistException e){
            return Util.getErrorBody(e.getMessage(), new StockTopFiveAllRes<List<StockInfoRes>>());
        }
    }
    @GetMapping("stockDetailTopHits")
    public BaseDto<List<StockInfoRes>> stockDetailTopHits(@RequestParam int pageNum , @RequestParam int pageSize, @RequestParam String nation) {
        try{
            List<StockInfoRes> stockDetailTopHitsRes = stockInfoService.stockDetailTopHits(pageNum, pageSize, nation);

            return BaseDto.<List<StockInfoRes>>builder()
                    .status(CUSTOM_CODE.RSEULT.SUCCESS.STATUS())
                    .rsltMsg("")
                    .rsltData(stockDetailTopHitsRes)
                    .build();
        }
        catch(QueryNoExistException e){
            return Util.getErrorBody(e.getMessage(), new ArrayList<StockInfoRes>());
        }
    }

    @GetMapping("stockDetailTopTradingVolume")
    public BaseDto<List<StockInfoRes>> stockDetailTopTradingVolume(@RequestParam int pageNum , @RequestParam int pageSize, @RequestParam String nation) {
        try{
            List<StockInfoRes> stockDetailTopHitsRes = stockInfoService.stockDetailTopTradingVolume(pageNum, pageSize, nation);

            return BaseDto.<List<StockInfoRes>>builder()
                    .status(CUSTOM_CODE.RSEULT.SUCCESS.STATUS())
                    .rsltMsg("")
                    .rsltData(stockDetailTopHitsRes)
                    .build();
        }
        catch(QueryNoExistException e){
            return Util.getErrorBody(e.getMessage(), new ArrayList<StockInfoRes>());
        }
    }

    @GetMapping("stockDetailTopGrowthRate")
    public BaseDto<List<StockInfoRes>> stockDetailTopGrowthRate(@RequestParam int pageNum , @RequestParam int pageSize, @RequestParam String nation) {
        try{
            List<StockInfoRes> stockDetailTopHitsRes = stockInfoService.stockDetailTopGrowthRate(pageNum, pageSize, nation);

            return BaseDto.<List<StockInfoRes>>builder()
                    .status(CUSTOM_CODE.RSEULT.SUCCESS.STATUS())
                    .rsltMsg("")
                    .rsltData(stockDetailTopHitsRes)
                    .build();
        }
        catch(QueryNoExistException e){
            return Util.getErrorBody(e.getMessage(), new ArrayList<StockInfoRes>());
        }
    }

    @GetMapping("stockDetailBottomGrowthRate")
    public BaseDto<List<StockInfoRes>> stockDetailBottomGrowthRate(@RequestParam int pageNum , @RequestParam int pageSize, @RequestParam String nation) {
        try{
            List<StockInfoRes> stockDetailTopHitsRes = stockInfoService.stockDetailBottomGrowthRate(pageNum, pageSize, nation);

            return BaseDto.<List<StockInfoRes>>builder()
                    .status(CUSTOM_CODE.RSEULT.SUCCESS.STATUS())
                    .rsltMsg("")
                    .rsltData(stockDetailTopHitsRes)
                    .build();
        }
        catch(QueryNoExistException e){
            return Util.getErrorBody(e.getMessage(), new ArrayList<StockInfoRes>());
        }
    }
}
