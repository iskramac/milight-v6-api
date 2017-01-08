package com.jeefix.iot.milight.common;

/**
 * API exception thrown when illegal argument has been passed
 * Created by Maciej Iskra (iskramac) on 2017-01-02.
 */
public class MilightArgumentException extends MilightException{
    public MilightArgumentException() {
    }

    public MilightArgumentException(String message) {
        super(message);
    }

    public MilightArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public MilightArgumentException(Throwable cause) {
        super(cause);
    }

    public MilightArgumentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
