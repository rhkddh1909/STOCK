package com.example.stockapi.api.marketinfo;

import com.example.stockapi.api.util.BaseDto;
import com.example.stockapi.api.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.stockapi.api.util.CUSTOM_CODE.RSEULT.SUCCESS;

@RestController
@RequiredArgsConstructor
public class MarketInfoContoller {
    private final MarketInfoService marketInfoService;

    @GetMapping("marketListAll")
    public BaseDto<List<MarketInfoRes>> getMarketList() {
        try{
            List<MarketInfoRes> marketListAll = marketInfoService.getMarketList();

            return BaseDto.<List<MarketInfoRes>>builder()
                    .status(SUCCESS.CODE())
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
                    .status(SUCCESS.CODE())
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
                    .status(SUCCESS.CODE())
                    .rsltMsg("")
                    .rsltData(marketInfoByNation)
                    .build();

        }catch(Exception e){
            return Util.getErrorBody(e.getMessage(), new ArrayList<MarketInfoRes>());
        }
    }

    @GetMapping("openMarket")
    public BaseDto<Object> openMarket(String nation) {
        try{

            Long updateCount = marketInfoService.openMarket(nation);

            return BaseDto.<Object>builder()
                    .status(SUCCESS.CODE())
                    .rsltMsg(updateCount+"개의 "+ Optional.ofNullable(nation).orElse("KOR")+"시장을 오픈하였습니다.")
                    .build();
        }
        catch(Exception e){
            return Util.getErrorBody(e.getMessage(), new Object());
        }
    }
    @GetMapping("closeMarket")
    public BaseDto<Object> closeMarket(String nation) {
        try{

            Long updateCount = marketInfoService.closeMarket(nation);

            return BaseDto.<Object>builder()
                    .status(SUCCESS.CODE())
                    .rsltMsg(updateCount+"개의 "+ Optional.ofNullable(nation).orElse("KOR")+"시장을 종료하였습니다.")
                    .build();
        }
        catch(Exception e){
            return Util.getErrorBody(e.getMessage(), new Object());
        }
    }
}
