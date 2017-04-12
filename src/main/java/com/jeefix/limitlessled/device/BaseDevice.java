package com.jeefix.limitlessled.device;

import com.jeefix.iot.milight.common.HexUtils;
import com.jeefix.iot.milight.common.MilightCommand;
import com.jeefix.iot.milight.common.MilightException;
import com.jeefix.limitlessled.session.SessionService;
import com.jeefix.limitlessled.session.SessionState;
import com.jeefix.limitlessled.transport.SimpleTransportService;
import com.jeefix.limitlessled.transport.TransportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * TODO write class description here
 * <p>
 * Created by Maciej Iskra (emacisk) on 2017-04-12.
 */
public abstract class BaseDevice {

    private static final Logger log = LoggerFactory.getLogger(BaseDevice.class);

    private AtomicInteger sequenceNumber = new AtomicInteger();

    protected TransportService transportService;
    protected SessionService sessionService;
    protected byte zoneId;


    protected void sendCommand(String hexCommand) {
        byte[] request = prepareCommand(hexCommand);
        transportService.sendPackage(request, new ResponseValidator(request));
    }


    protected byte[] prepareCommand(String commandTypeHex) {
        // '80 00 00 00 11(length hex) (17 01)(WB1WB2) 00 SN 00 (31 00 00 08 04 01 00 00 00)(cmd) 01(zone) 00 3F(chksum) response: (88 00 00 00 03 00 SN 00)
        String commandTemplate = String.format("80 00 00 00 04 05 06 00 08 00 %s 19 00 21", commandTypeHex);
        SessionState sessionState = sessionService.getSessionState();
        byte[] command = HexUtils.getStringAsHex(commandTemplate);
        command[4] = (byte) (command.length - 5);
        command[5] = sessionState.getSessionId1();
        command[6] = sessionState.getSessionId2();
        command[8] = (byte) getSequenceNumber();
        command[19] = zoneId;
        command[21] = computeChecksum(command);
        return command;
    }

    protected static byte computeChecksum(byte[] messageBytes) {
        byte sum = 0;
        for (int i = 2; i <= 12; i++) {
            sum += messageBytes[messageBytes.length - i];
        }
        return sum;
    }


    protected int getSequenceNumber() {
        if (sequenceNumber.get() >= 255) {
            sequenceNumber.set(0);
        }
        return sequenceNumber.getAndIncrement();
    }


    protected static class ResponseValidator implements Consumer<byte[]> {

        private byte[] request;

        public ResponseValidator(byte[] request) {
            this.request = request;
        }

        @Override
        public void accept(byte[] response) {
            if (response[0] != request[0] + 8 || response.length != response[4] + 5) {
                throw new MilightException(String.format("Received error response from bridge: %s", response));
            }
        }
    }

    public TransportService getTransportService() {
        return transportService;
    }

    public void setTransportService(TransportService transportService) {
        this.transportService = transportService;
    }

    public SessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public byte getZoneId() {
        return zoneId;
    }

    public void setZoneId(byte zoneId) {
        this.zoneId = zoneId;
    }
}
