package io.ib67.util;

import io.ib67.exception.ValidationException;

import java.math.BigInteger;

public class Validation {
    private Validation(){

    }
    public static void notNull(Object obj,String message){
        if(obj==null){
            throw new NullPointerException(message);
        }
    }
    public static void checkTrue(boolean condition,String message){
        if(!condition){
            throw new ValidationException(message);
        }
    }
    public static void checkFalse(boolean condition,String message){
        if(condition){
            throw new ValidationException(message);
        }
    }
    public static void isNumber(String v,String message){
        try {
            new BigInteger(v);
        }catch(Throwable t){
            throw new ValidationException(message);
        }
    }
}
