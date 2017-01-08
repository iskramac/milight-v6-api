package com.jeefix.iot.milight;

import com.jeefix.iot.milight.common.HexUtils;
import com.jeefix.iot.milight.common.MilightArgumentException;
import com.jeefix.iot.milight.common.MilightCommand;
import com.jeefix.iot.milight.transport.MessageTransportService;
import org.assertj.core.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests of {@link CommandService}
 * Created by Maciej Iskra (iskramac) on 2017-01-03.
 */
public class CommandServiceTest {

    private CommandService commandService;
    private MessageTransportService mts;

    @BeforeClass
    public void init() {
        commandService = new CommandService();
        mts = mock(MessageTransportService.class);
        commandService.setTransportService(mts);
    }

    @Test
    public void shouldInitializeProperly() {
        byte[] sessionCreateRequest = HexUtils.getStringAsHex("20 00 00 00 16 02 62 3A D5 ED A3 01 AE 08 2D 46 61 41 A7 F6 DC AF D3 E6 00 00 1E");
        byte[] sessionCreateResponse = HexUtils.getStringAsHex("28 00 00 00 11 00 02 F0 FE 6B 14 B7 68 42 14 22 DE 00 01 9B 00 00");
        ArgumentCaptor<Consumer<byte[]>> argument = ArgumentCaptor.forClass(Consumer.class);

        commandService.init();

        verify(commandService.getTransportService()).sendPackage(anyInt(), eq(sessionCreateRequest), argument.capture());
        argument.getValue().accept(sessionCreateResponse);


        assertThat(commandService.sessionId1).isEqualTo((byte) 0x9b);
        assertThat(commandService.sessionId2).isEqualTo((byte) 0x00);
        assertThat(commandService.isInitialized()).isEqualTo(true);
    }

    @Test(dependsOnMethods = "shouldInitializeProperly")
    public void shouldSendCommandProperly() {

        byte[] turnOnRequest = HexUtils.getStringAsHex("80 00 00 00 11 9B 00 00 01 00 31 00 00 08 04 01 00 00 00 00 00 3E");
        reset(commandService.getTransportService());
        commandService.turnOn();
        verify(commandService.getTransportService()).sendPackage(anyInt(), eq(turnOnRequest));
    }

    @Test(dependsOnMethods = "shouldInitializeProperly")
    public void shouldSetBrightness() {
        reset(commandService.getTransportService());
        commandService.setBrightness(0);
        verify(commandService.getTransportService()).sendPackage(anyInt(), argThat((arg) -> {
            HexUtils.getHexAsString(arg).contains(String.format(MilightCommand.BRIGHTNESS_SET.getHexCommand(), 0));
            return true;
        }));
        reset(commandService.getTransportService());
        commandService.setBrightness(50);
        verify(commandService.getTransportService()).sendPackage(anyInt(), argThat((arg) -> {
            HexUtils.getHexAsString(arg).contains(String.format(MilightCommand.BRIGHTNESS_SET.getHexCommand(), 32));
            return true;
        }));

        reset(commandService.getTransportService());
        commandService.setBrightness(100);
        verify(commandService.getTransportService()).sendPackage(anyInt(), argThat((arg) -> {
            HexUtils.getHexAsString(arg).contains(String.format(MilightCommand.BRIGHTNESS_SET.getHexCommand(), 64));
            return true;
        }));

        assertThatThrownBy(() -> commandService.setBrightness(101)).isInstanceOf(MilightArgumentException.class).hasMessageContaining("101");
        assertThatThrownBy(() -> commandService.setBrightness(-1)).isInstanceOf(MilightArgumentException.class).hasMessageContaining("-1");


    }
}
