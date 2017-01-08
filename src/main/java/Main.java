import com.jeefix.iot.milight.CommandService;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class Main {
    private static DatagramSocket clientSocket;
    private static InetAddress IPAddress;



    public static void main(String[] args) throws Exception {

        CommandService commandService = new CommandService("172.23.4.225", 0);

       commandService.newMilightFlow().on().brightnessLevel(64).delay(1000).brightnessLevel(32).delay(500).brightnessLevel(16).delay(500).off();



    }




}
