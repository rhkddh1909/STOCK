package com.example.stockapi.api.stock;

import com.example.stockapi.api.util.BaseDto;
import com.example.stockapi.api.util.CUSTOM_CODE;
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
        List<StockInfoRes> stockInfoRes = stockInfoService.stockInfos();

        return BaseDto.<List<StockInfoRes>>builder()
                .status(CUSTOM_CODE.RSEULT.SUCCESS.STATUS())
                .rsltMsg("")
                .rsltData(stockInfoRes)
                .build();
    }
}
