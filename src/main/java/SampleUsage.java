import com.jeefix.limitlessled.device.LedStripDevice;

public class SampleUsage {

    public static void main(String[] args) throws Exception {
        LedStripDevice commandService = new LedStripDevice("172.23.4.225", 0); //set bridge IP, zone number

        commandService
                .on() //turn lights on
                .hue(0) //set hue to 0 (near red color)
                .saturation(0) //set saturation level to 0%
                .brightness(100) //set brightness to 100%
                .whiteOn() //turn on white led
                .off();
    }
}
