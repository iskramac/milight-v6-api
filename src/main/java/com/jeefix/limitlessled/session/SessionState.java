package com.jeefix.limitlessled.session;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * TODO write class description here
 * <p>
 * Created by Maciej Iskra (emacisk) on 2017-04-12.
 */
public class SessionState {

    protected byte sessionId1;
    protected byte sessionId2;
    private LocalDateTime lastRefresh;

    public SessionState(byte sessionId1, byte sessionId2) {
        this.sessionId1 = sessionId1;
        this.sessionId2 = sessionId2;
        lastRefresh = LocalDateTime.now();
    }

    public byte getSessionId1() {
        return sessionId1;
    }

    public byte getSessionId2() {
        return sessionId2;
    }

    @Override
    public String toString() {
        return "SessionState{" +
                "sessionId1=" + sessionId1 +
                ", sessionId2=" + sessionId2 +
                ", lastRefresh=" + lastRefresh +
                '}';
    }
}
