package com.jeefix.iot.milight.common;

/**
 * Default API exception
 * Created by Maciej Iskra (iskramac) on 2017-01-02.
 */
public class MilightException extends RuntimeException{
    public MilightException() {
    }

    public MilightException(String message) {
        super(message);
    }

    public MilightException(String message, Throwable cause) {
        super(message, cause);
    }

    public MilightException(Throwable cause) {
        super(cause);
    }

    public MilightException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
