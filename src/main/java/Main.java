import com.jeefix.iot.milight.CommandService;
import com.jeefix.iot.milight.api.FluentCommandWrapper;

public class Main {

    public static void main(String[] args) throws Exception {
        CommandService commandService = new CommandService("172.23.4.225", 0); //set bridge IP, zone number
        FluentCommandWrapper fluentCommandWrapper = commandService.newMilightFlow()
                .on() //turn lights on
                .hue(0)
                .brightnessLevel(100) //set brightness to 100%
                .delay(1000) //wait 1 second
                .brightnessLevel(50) //set brightness to 50%
                .delay(500)
                .brightnessLevel(25)
                .off();
    }
}
