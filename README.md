## API for milight iBox WIFI controller (Limitless Led API v6)
Library provides Java API for milight/limitless WIFI bridge  of version 6 (Milight iBox)

### Usage
```java
import com.jeefix.iot.milight.CommandService;

public class SampleUsage {

    public static void main(String[] args) throws Exception {
        CommandService commandService = new CommandService("172.23.4.225", 0); //set bridge IP, zone number
               commandService.newMilightFlow()
                       .on() //turn lights on
                       .hue(0) //set hue to 0 (near red color)
                       .saturationLevel(0) //set saturation level to 0%
                       .brightnessLevel(100) //set brightness to 100%
                       .delay(1000) //wait 1 second
                       .brightnessLevel(50) //set brightness to 50%
                       .delay(500)
                       .brightnessLevel(25)
                       .delay(500)
                       .whiteOn() //turn on white led
                       .off();
    }
}
```
