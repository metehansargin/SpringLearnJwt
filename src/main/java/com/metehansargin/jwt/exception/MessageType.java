package com.metehansargin.jwt.exception;

import lombok.Getter;

@Getter
public enum MessageType {
    NO_USER("10001","User Bulunamadi"),
    GENERAL_EXCEPTION("9999","Gel bir hata olustu");

    private String code;
    private String message;

    MessageType(String code,String message){
        this.code=code;
        this.message=message;
    }
}
