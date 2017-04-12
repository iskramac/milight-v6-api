import com.jeefix.limitlessled.device.IBoxDevice;
import com.jeefix.limitlessled.device.LedStripDevice;
import com.jeefix.limitlessled.utils.DeviceFactory;

public class SampleUsage {

    public static void main(String[] args) throws Exception {

        DeviceFactory deviceFactory = new DeviceFactory("172.23.4.225");
        LedStripDevice ledStripDevice = deviceFactory.getDevice(LedStripDevice.class, 0);

        ledStripDevice
                .on() //turn lights on
                .hue(0) //set hue to 0 (near red color)
                .saturation(0) //set saturation level to 0%
                .brightness(100) //set brightness to 100%
                .whiteOn() //turn on white led
                .off();

        IBoxDevice iBoxDevice = deviceFactory.getDevice(IBoxDevice.class, 0);

        iBoxDevice
                .on()
                .brightness(100)
                .hue(0)
                .brightness(100)
                .whiteOn()
                .off();


    }
}
