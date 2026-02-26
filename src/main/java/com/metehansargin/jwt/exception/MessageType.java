package com.metehansargin.jwt.exception;

import lombok.Getter;

@Getter
public enum MessageType {
    NO_USER("10000","User Bulunamadi"),
    NO_EMPLOYEE("10001","Employee Bulunamadi"),
    TOKEN_EXPIRED("10002","Token Suresi Doldu"),
    GENERAL_EXCEPTION("9999","Gel bir hata olustu");

    private String code;
    private String message;

    MessageType(String code,String message){
        this.code=code;
        this.message=message;
    }
}
