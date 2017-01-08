package com.jeefix.iot.milight.api;

import com.jeefix.iot.milight.CommandService;

/**
 * Provides fluent API for {@link CommandService}
 * Created by Maciej Iskra (iskramac) on 2017-01-03.
 */
public class FluentCommandWrapper {

    private CommandService commandService;

    public FluentCommandWrapper(CommandService commandService) {
        this.commandService = commandService;
    }

    /**
     * Turns on LED
     *
     * @return this
     */
    public FluentCommandWrapper on() {
        commandService.turnOn();
        return this;
    }

    /**
     * Turns off LED
     *
     * @return this
     */
    public FluentCommandWrapper off() {
        commandService.turnOff();
        return this;
    }

    /**
     * Sets brightness level to selected value
     *
     * @param level of brightness in rage 0-100
     * @return this
     */
    public FluentCommandWrapper brightnessLevel(int level) {
        commandService.setBrightness(level);
        return this;
    }

    /**
     * Delays execution of nex command
     *
     * @param timeInMilis to delay
     * @return this
     */
    public FluentCommandWrapper delay(int timeInMilis) {
        try {
            Thread.sleep(timeInMilis);
        } catch (InterruptedException e) {
            throw new RuntimeException("Thread has ben interrupted", e);
        }
        return this;
    }

    /**
     * Sets hue level to selected value
     *
     * @param level of hue in rage 0-360
     * @return this
     */
    public FluentCommandWrapper hue(int level) {
        commandService.setHue(level);
        return this;
    }
}
