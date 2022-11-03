package com.example.stockapi.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class Util {
    public static Function<Long, Long> checkPrice = (price)->{
        if(price < 1000L) return 1L;
        if(price < 5000L) return 5L;
        if(price < 10000L) return 10L;
        if(price < 50000L) return 50L;
        if(price < 100000L) return 100L;
        if(price < 500000L) return 500L;
        if(500000L < price) return 1000L;
        return 0L;
    };

    public static Function<Double,Long> checkGrowthRate = (rate)->{
        if(rate < 10) return 100L;
        if(rate < 20) return 500L;
        if(20 <= rate) return 1000L;
        return 0L;
    };

    public static <T> BaseDto<T> getErrorBody(String Message, T t){
        ObjectMapper mapper = new ObjectMapper();
        return BaseDto.<T>builder()
                .status(CUSTOM_CODE.RSEULT.ERROR.STATUS())
                .rsltMsg(Message)
                .build();

    }

    /**
     * 상승률
     * */
    public static Double getGrowthRate(Long startingPrice, Long currentPrice) {
        Double tmpStartingPrice = Double.valueOf(startingPrice);
        Double tmpCurrentPrice = Double.valueOf(currentPrice);

        return Double.valueOf(String.format("%.2f",((tmpCurrentPrice - tmpStartingPrice)/tmpStartingPrice) * 100.0));
    }


    public static Long getAskingPrice(Long currentPrice, Long buyingCount, Long sellingCount) {
        Long from = buyingCount > sellingCount ? -3L : -5L;
        Long to = buyingCount > sellingCount ? 5L : 3L;
        Long random = ThreadLocalRandom.current().nextLong(from,to);

        return checkPrice.apply(currentPrice)* random;
    }

    public static Long getHitsCount(Long startingPrice, Long currentPrice) {
        Long rateCheckingNum = checkGrowthRate.apply(Math.abs(getGrowthRate(startingPrice,currentPrice)));

        return ThreadLocalRandom.current().nextLong(rateCheckingNum/10L,rateCheckingNum);
    }

    public static Long getBuyingSellingCount(Long startingPrice, Long currentPrice){
        Long rateCheckingNum = checkGrowthRate.apply(Math.abs(getGrowthRate(startingPrice,currentPrice)));

        return ThreadLocalRandom.current().nextLong(rateCheckingNum/10L,rateCheckingNum*5L);
    }
}
