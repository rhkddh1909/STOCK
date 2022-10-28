package com.example.stockapi.api.util;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BaseDto<T> {
    private String status;
    private String rsltMsg;
    private T rsltData;
}
