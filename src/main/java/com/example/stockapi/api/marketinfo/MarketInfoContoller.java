package com.example.stockapi.api.marketinfo;

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
public class MarketInfoContoller {
    private final MarketInfoService marketInfoService;

    @GetMapping("marketListAll")
    public BaseDto<List<MarketInfoRes>> getMarketList() {
        try{
            List<MarketInfoRes> marketListAll = marketInfoService.getMarketList();

            return BaseDto.<List<MarketInfoRes>>builder()
                    .status(CUSTOM_CODE.RSEULT.SUCCESS.STATUS())
                    .rsltMsg("")
                    .rsltData(marketListAll)
                    .build();

        }catch(Exception e){
            return Util.getErrorBody(e.getMessage(), new ArrayList<MarketInfoRes>());
        }

    }

    @GetMapping("marketInfoByCode")
    public BaseDto<MarketInfoRes> getMarketInfoByCode(@RequestParam String marketCode) {
        try{
            MarketInfoRes marketInfoByCode = marketInfoService.getMarketInfoByCode(marketCode);

            return BaseDto.<MarketInfoRes>builder()
                    .status(CUSTOM_CODE.RSEULT.SUCCESS.STATUS())
                    .rsltMsg("")
                    .rsltData(marketInfoByCode)
                    .build();

        }catch(Exception e){
            return Util.getErrorBody(e.getMessage(), new MarketInfoRes());
        }
    }

    @GetMapping("marketInfoByNation")
    public BaseDto<List<MarketInfoRes>> getMarketInfoByNation(@RequestParam String marketNation) {
        try{
            List<MarketInfoRes> marketInfoByNation = marketInfoService.getMarketInfoByNation(marketNation);

            return BaseDto.<List<MarketInfoRes>>builder()
                    .status(CUSTOM_CODE.RSEULT.SUCCESS.STATUS())
                    .rsltMsg("")
                    .rsltData(marketInfoByNation)
                    .build();

        }catch(Exception e){
            return Util.getErrorBody(e.getMessage(), new ArrayList<MarketInfoRes>());
        }
    }
}
