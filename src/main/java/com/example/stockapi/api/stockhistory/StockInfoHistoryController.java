package com.example.stockapi.api.stockhistory;

import com.example.stockapi.api.util.BaseDto;
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
public class StockInfoHistoryController {
    private final StockInfoHistoryService stockInfoHistoryService;
    @GetMapping("stockInfoHistories")
    public BaseDto<List<StockInfoHistoryDto>> stockInfoHistories(String nation, String stockCode) {
        try{
            List<StockInfoHistoryDto> stockInfoHistoryDtos = stockInfoHistoryService.stockInfoHistories(nation,stockCode);

            return BaseDto.<List<StockInfoHistoryDto>>builder()
                    .status(SUCCESS.CODE())
                    .rsltMsg("")
                    .rsltData(stockInfoHistoryDtos)
                    .build();
        }
        catch(Exception e){
            return Util.getErrorBody(e.getMessage(),new ArrayList<>());
        }
    }
    @GetMapping("stockInfoHistoryAnalysis")
    public BaseDto<List<StockInfoHistoryDto>> stockInfoHistoryAnalysis(String nation, String stockCode, @RequestParam long analysisTerm) {
        try{
            List<StockInfoHistoryDto> stockInfoHistoryAnalysisDtos = stockInfoHistoryService.stockInfoHistoryAnalysis(nation,stockCode,analysisTerm);

            return BaseDto.<List<StockInfoHistoryDto>>builder()
                    .status(SUCCESS.CODE())
                    .rsltMsg("")
                    .rsltData(stockInfoHistoryAnalysisDtos)
                    .build();
        }
        catch(Exception e){
            return Util.getErrorBody(e.getMessage(),new ArrayList<>());
        }
    }
}
