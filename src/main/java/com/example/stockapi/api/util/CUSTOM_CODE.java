package com.example.stockapi.api.util;

public class CUSTOM_CODE {
    public enum RSEULT{
        SUCCESS("0000"),
        ERROR("9999");

        private String status;

        RSEULT(String status) {
            this.status = status;
        }
        public String STATUS(){
            return status;
        }
    }
}
