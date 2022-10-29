package com.example.stockapi.api.util;

public class Util {
    public static BaseDto getErrorBody(String Message){
        return BaseDto.builder()
                .status(CUSTOM_CODE.RSEULT.ERROR.STATUS())
                .rsltMsg(Message)
                .build();
    }
}
