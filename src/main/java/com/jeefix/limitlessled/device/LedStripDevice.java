package com.jeefix.limitlessled.device;

import com.jeefix.iot.milight.common.MilightArgumentException;
import com.jeefix.iot.milight.common.MilightCommand;
import com.jeefix.limitlessled.device.state.LedStripState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO write class description here
 * <p>
 * Created by Maciej Iskra (emacisk) on 2017-04-12.
 */
public class LedStripDevice extends BaseDevice {

    private static final Logger log = LoggerFactory.getLogger(LedStripDevice.class);

    public static final int HUE_MAX_COLOR = 360;

    private LedStripState state;

    public LedStripDevice() {
        state = new LedStripState();
    }

    public LedStripDevice on() {
        log.debug("Attempting to turn on device {}", this);
        sendCommand(MilightCommand.LED_ON.getHexCommand());
        state.setOn(true);
        log.info("Turned on device {}", this);
        return this;
    }

    public LedStripDevice off() {
        log.debug("Attempting to turn off device {}", this);
        sendCommand(MilightCommand.LED_OFF.getHexCommand());
        state.setOn(false);
        log.info("Turned off device {}", this);
        return this;
    }

    public LedStripDevice brightness(int brightness) {
        log.debug("Attempting to set brightness level to {} of device {}", brightness, this);
        if (brightness < 0 || brightness > 100) {
            throw new MilightArgumentException(String.format("Brightness level should be in range 0-100. Received %d", brightness));
        }
        int normalizedValue = (int) Math.ceil((double) brightness * 64 / 100);
        String command = String.format(MilightCommand.BRIGHTNESS_SET.getHexCommand(), normalizedValue);
        sendCommand(command);
        state.setBrightness(brightness);
        log.info("Changed brightness level to {} of device {}", brightness, this);
        return this;
    }

    public LedStripDevice hue(int hue) {
        log.debug("Attempting to set hue level to {} of device {}", hue, this);
        int color = (int) (((float) hue / HUE_MAX_COLOR) * 255);
        String command =String.format(MilightCommand.HUE_SET.getHexCommand(), color, color, color, color);
        sendCommand(command);
        state.setHue(hue);
        log.info("Changed hue level to {} of device {}", hue, this);
        return this;
    }

    public LedStripDevice saturation(int saturation) {
        log.debug("Attempting to set saturation level to {} of device {}", saturation, this);
        if (saturation < 0 || saturation > 100) {
            throw new MilightArgumentException(String.format("Saturation level should be in range 0-100. Received %d", saturation));
        }
        int normalizedValue = (int) Math.ceil((double) saturation * 64 / 100);
        String command = String.format(MilightCommand.SATURATION_SET.getHexCommand(), normalizedValue);
        sendCommand(command);
        state.setSaturation(saturation);
        log.info("Changed saturation level to {} of device {}", saturation, this);
        return this;
    }

    public LedStripDevice whiteOn() {
        log.debug("Attempting to turn white mode of device {}", this);
        sendCommand(MilightCommand.WHITE_ON.getHexCommand());
        state.setWhite(true);
        log.debug("Turned white mode of device {}", this);
        return this;
    }
}
