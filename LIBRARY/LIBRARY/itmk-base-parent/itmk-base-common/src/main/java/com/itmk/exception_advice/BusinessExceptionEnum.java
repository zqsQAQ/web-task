package com.itmk.exception_advice;

public enum  BusinessExceptionEnum {
    SERVER_ERROR(500, "Server exception！"),
    NO_STOCK(1001,"---->Stock shortage!"),
    ;

    private Integer code;
    private String message;

    BusinessExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
