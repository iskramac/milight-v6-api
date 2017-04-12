package com.jeefix.limitlessled.device.state;

/**
 * TODO write class description here
 * <p>
 * Created by Maciej Iskra (emacisk) on 2017-04-12.
 */
public class LedStripState extends DeviceState {

    private int brightness;
    private int hue;
    private int saturation;
    private boolean white;

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getHue() {
        return hue;
    }

    public void setHue(int hue) {
        this.hue = hue;
        this.white = false;
    }

    public int getSaturation() {
        return saturation;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
        this.white = false;
    }

    public boolean isWhite() {
        return white;
    }

    public void setWhite(boolean white) {
        this.white = white;
    }
}
