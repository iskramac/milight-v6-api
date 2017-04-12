package com.jeefix.limitlessled.utils;

import com.jeefix.iot.milight.common.MilightException;
import com.jeefix.limitlessled.device.BaseDevice;
import com.jeefix.limitlessled.device.LedStripDevice;
import com.jeefix.limitlessled.session.SessionService;
import com.jeefix.limitlessled.transport.SimpleTransportService;
import com.jeefix.limitlessled.transport.TransportService;

/**
 * TODO write class description here
 * <p>
 * Created by Maciej Iskra (emacisk) on 2017-04-12.
 */
public class DeviceFactory {

    private SessionService sessionService;
    private TransportService transportService;

    public static final int COMMUNICATION_PORT = 5987;

    public DeviceFactory(String ip) {
        transportService = new SimpleTransportService(ip, COMMUNICATION_PORT);
        sessionService = new SessionService(transportService);
    }

    public <T extends BaseDevice> T getDevice(Class<T> deviceType, int zone) {
        try {
            T instance = deviceType.newInstance();
            instance.setZoneId((byte) zone);
            instance.setTransportService(transportService);
            instance.setSessionService(sessionService);
            return instance;
        } catch (Exception e) {
            throw new MilightException(String.format("Unable to create instance of device %s", deviceType), e);
        }

    }
}
