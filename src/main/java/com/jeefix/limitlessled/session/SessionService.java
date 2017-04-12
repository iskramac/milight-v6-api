package com.jeefix.limitlessled.session;

import com.jeefix.iot.milight.common.HexUtils;
import com.jeefix.iot.milight.common.MilightCommand;
import com.jeefix.limitlessled.transport.TransportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO write class description here
 * <p>
 * Created by Maciej Iskra (emacisk) on 2017-04-12.
 */
public class SessionService {

    private static final Logger log = LoggerFactory.getLogger(SessionService.class);

    private SessionState sessionState;

    private TransportService transportService;

    public SessionService(TransportService transportService) {
        this.transportService = transportService;
        byte[] createSessionRequest = HexUtils.getStringAsHex(MilightCommand.CREATE_SESSION.getHexCommand());
        transportService.sendPackage(createSessionRequest, (response) -> {
            sessionState = new SessionState(response[19], response[20]);
            log.info("Successfully Created new session: {}", sessionState);
        });
    }

    public void keepAlive() {
        log.debug("Attempting to send keep alive packet to device {}", this);
        byte[] requestS1 = HexUtils.getStringAsHex(String.format(MilightCommand.KEEP_ALIVE.getHexCommand(), getSessionState().getSessionId1()));
        byte[] requestS2 = HexUtils.getStringAsHex(String.format(MilightCommand.KEEP_ALIVE.getHexCommand(), getSessionState().getSessionId2()));

        transportService.sendPackage(requestS1, response -> {
            log.debug("Refreshed session id1: {}", response);
        });

        transportService.sendPackage(requestS2, response -> {
            log.debug("Refreshed session id2: {}", response);
        });
        log.debug("Sent keep alive packet to device {}", this);
    }

    public SessionState getSessionState() {
        return sessionState;
    }
}
