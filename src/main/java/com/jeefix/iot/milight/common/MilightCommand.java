package com.jeefix.iot.milight.common;

/**
 * Contains codes of Milight v6 bridge commands.
 * Acquired from official documentation:
 * See <a href="http://www.limitlessled.com/dev/">http://www.limitlessled.com/dev/</a>
 * Created by Maciej Iskra (iskramac) on 2017-01-02.
 */
public enum MilightCommand {

    /**
     * Creates session with a bridge
     */
    CREATE_SESSION("20 00 00 00 16 02 62 3A D5 ED A3 01 AE 08 2D 46 61 41 A7 F6 DC AF D3 E6 00 00 1E"),

    /**
     * Sets leds on
     */
    LED_ON("31 00 00 08 04 01 00 00 00"),

    /**
     * Sets leds off
     */
    LED_OFF("31 00 00 08 04 02 00 00 00"),

    /**
     * Set brightness level. This command is parametrized! Change first occurrence of '%02d' to level in range 0-64
     */
    BRIGHTNESS_SET("31 00 00 08 03 %02d 00 00 00"),

    /**
     * Set color. This command is parametrized! Change four  occurrences of '%02d' to milight color
     */
    COLOR_SET("31 00 00 08 01 %02X %02X %02X %02X");

    private String hexCommand;

    MilightCommand(String hexCommand) {
        this.hexCommand = hexCommand;
    }

    public String getHexCommand() {
        return hexCommand;
    }

}
