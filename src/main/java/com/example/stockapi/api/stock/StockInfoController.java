package com.example.stockapi.api.stock;

import com.example.stockapi.api.exception.StockNoExistException;
import com.example.stockapi.api.util.BaseDto;
import com.example.stockapi.api.util.CUSTOM_CODE;
import com.example.stockapi.api.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StockInfoController {
    private final StockInfoService stockInfoService;
    @GetMapping("stockInfos")
    public BaseDto<List<StockInfoRes>> getStockInfoList() {
        try {
            List<StockInfoRes> stockInfoRes = stockInfoService.stockInfos();

            return BaseDto.<List<StockInfoRes>>builder()
                    .status(CUSTOM_CODE.RSEULT.SUCCESS.STATUS())
                    .rsltMsg("")
                    .rsltData(stockInfoRes)
                    .build();
        }
        catch(StockNoExistException e){
            return Util.getErrorBody(e.getMessage());
        }
    }
    @GetMapping("reRanking")
    public BaseDto reRanking() {
        try{
            Long reRankCount = stockInfoService.reRanking();

            return BaseDto.builder()
                    .status(CUSTOM_CODE.RSEULT.SUCCESS.STATUS())
                    .rsltMsg(reRankCount+"건의 데이터가 변동되었습니다.")
                    .build();
        }
        catch(StockNoExistException e){
            return Util.getErrorBody(e.getMessage());
        }
    }
}
