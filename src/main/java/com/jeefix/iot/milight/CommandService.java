package com.jeefix.iot.milight;

import com.jeefix.iot.milight.api.FluentCommandWrapper;
import com.jeefix.iot.milight.common.HexUtils;
import com.jeefix.iot.milight.common.MilightArgumentException;
import com.jeefix.iot.milight.common.MilightCommand;
import com.jeefix.iot.milight.common.MilightException;
import com.jeefix.iot.milight.transport.MessageTransportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.function.Consumer;

/**
 * Handles high level communication with device
 * Created by Macie Iskra (iskramac) on 2017-01-02.
 */
public class CommandService {

    private static final Logger log = LoggerFactory.getLogger(CommandService.class);

    public static final int ROUTER_COMMUNICATION_PORT = 48899;
    public static final int COMMUNICATION_PORT = 5987;
    public static final int HUE_MAX_COLOR = 360;

    private int sequenceNumber = 0;
    protected byte sessionId1;
    protected byte sessionId2;
    protected MessageTransportService transportService;
    protected byte zoneId;
    private boolean initialized;

    public CommandService() {
        log.info("Created default instance");
    }

    public CommandService(String bridgeIp, int zone) {
        transportService = new MessageTransportService(bridgeIp);
        this.zoneId = (byte) zone;
        init();
        log.info("Created instance for bridge with ip {} and zone {}", bridgeIp, zone);
    }

    public void init() {
        byte[] createSessionRequest = HexUtils.getStringAsHex(MilightCommand.CREATE_SESSION.getHexCommand());
        transportService.sendPackage(COMMUNICATION_PORT, createSessionRequest, (response) -> {
            sessionId1 = response[19];
            sessionId2 = response[20];
            initialized = true;
            log.info("Bridge has been successfully initialized");
        });

    }

    /**
     * That functionality is not working as supposed to (according to spec)
     */
    @Deprecated
    public void discoverBridges() {
        MessageTransportService mts = new MessageTransportService("255.255.255.255");
        mts.sendPackage(ROUTER_COMMUNICATION_PORT, "HF-A11 ASSISTHEAD".getBytes(Charset.forName("UTF-8")), (r) -> {
            System.out.println(r);
        });
    }

    public FluentCommandWrapper newMilightFlow() {
        if (isInitialized() == false) {
            throw new MilightException("Command service has not been initialized yet! Call init() method first.");
        }
        return new FluentCommandWrapper(this);
    }

    public void turnOn() {
        log.debug("Attempting to turn on device {}", this);
        byte[] request = prepareCommand(MilightCommand.LED_ON.getHexCommand());
        transportService.sendPackage(COMMUNICATION_PORT, request, new ResponseValidator(request));
        log.info("Turned on device {}", this);
    }

    public void turnOff() {
        log.debug("Attempting to turn off device {}", this);
        byte[] request = prepareCommand(MilightCommand.LED_OFF.getHexCommand());
        transportService.sendPackage(COMMUNICATION_PORT, request, new ResponseValidator(request));
        log.info("Turned off device {}", this);
    }

    public void setBrightness(int brightness) {
        log.debug("Attempting to set brightness level to {} of device {}", brightness, this);
        if (brightness < 0 || brightness > 100) {
            throw new MilightArgumentException(String.format("Brightness level should be in range 0-100. Received %d", brightness));
        }
        int normalizedValue = (int) Math.ceil((double) brightness * 64 / 100);
        byte[] request = prepareCommand(String.format(MilightCommand.BRIGHTNESS_SET.getHexCommand(), normalizedValue));
        transportService.sendPackage(COMMUNICATION_PORT, request, new ResponseValidator(request));
        log.info("Changed brightness level to {} of device {}", brightness, this);
    }

    public void setHue(int hue) {
        log.debug("Attempting to set hue level to {} of device {}", hue, this);
        int color = (int) (((float) hue / HUE_MAX_COLOR) * 255);
        byte[] request = prepareCommand(String.format(MilightCommand.HUE_SET.getHexCommand(), color, color, color, color));
        transportService.sendPackage(COMMUNICATION_PORT, request, new ResponseValidator(request));
        log.info("Changed hue level to {} of device {}", hue, this);
    }

    public void setSaturation(int saturation) {
        log.debug("Attempting to set saturation level to {} of device {}", saturation, this);
        if (saturation < 0 || saturation > 100) {
            throw new MilightArgumentException(String.format("Saturation level should be in range 0-100. Received %d", saturation));
        }
        int normalizedValue = (int) Math.ceil((double) saturation * 64 / 100);
        byte[] request = prepareCommand(String.format(MilightCommand.SATURATION_SET.getHexCommand(), normalizedValue));
        transportService.sendPackage(COMMUNICATION_PORT, request, new ResponseValidator(request));
        log.info("Changed saturation level to {} of device {}", saturation, this);
    }

    public void whiteOn() {
        log.debug("Attempting to turn white mode of device {}", this);
        byte[] request = prepareCommand(MilightCommand.WHITE_ON.getHexCommand());
        transportService.sendPackage(COMMUNICATION_PORT, request, new ResponseValidator(request));
        log.debug("Turned white mode of device {}", this);
    }

    public void keepAlive() {
        log.debug("Attempting to send keep alive packet to device {}", this);
        byte[] requestS1 = HexUtils.getStringAsHex(String.format(MilightCommand.KEEP_ALIVE.getHexCommand(), sessionId1));
        byte[] requestS2 = HexUtils.getStringAsHex(String.format(MilightCommand.KEEP_ALIVE.getHexCommand(), sessionId2));
        transportService.sendPackage(COMMUNICATION_PORT, requestS1, response -> {
            System.out.println(HexUtils.getHexAsString(response));
        });

        transportService.sendPackage(COMMUNICATION_PORT, requestS2, response -> {
            System.out.println(HexUtils.getHexAsString(response));
        });
        log.debug("Sent keep alive packet to device {}", this);

    }

    protected byte[] prepareCommand(String commandTypeHex) {
        // '80 00 00 00 11(length hex) (17 01)(WB1WB2) 00 SN 00 (31 00 00 08 04 01 00 00 00)(cmd) 01(zone) 00 3F(chksum) response: (88 00 00 00 03 00 SN 00)
        String commandTemplate = String.format("80 00 00 00 04 05 06 00 08 00 %s 19 00 21", commandTypeHex);
        byte[] command = HexUtils.getStringAsHex(commandTemplate);
        command[4] = (byte) (command.length - 5);
        command[5] = sessionId1;
        command[6] = sessionId2;
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
        if (sequenceNumber >= 255) {
            sequenceNumber = 0;
        } else {
            sequenceNumber++;
        }
        return sequenceNumber;
    }

    public MessageTransportService getTransportService() {
        return transportService;
    }

    public void setTransportService(MessageTransportService transportService) {
        if (this.transportService != null) {
            throw new MilightException("Transport Service has been already set!");
        }
        this.transportService = transportService;
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

    public byte getZoneId() {
        return zoneId;
    }

    public void setZoneId(byte zoneId) {
        this.zoneId = zoneId;
    }

    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", transportService.getIpAddress(), zoneId);
    }
}
