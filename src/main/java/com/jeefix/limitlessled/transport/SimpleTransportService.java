package com.jeefix.limitlessled.transport;

import com.jeefix.iot.milight.common.HexUtils;
import com.jeefix.iot.milight.common.MilightException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.function.Consumer;

/**
 * TODO write class description here
 * <p>
 * Created by Maciej Iskra (emacisk) on 2017-04-12.
 */
public class SimpleTransportService implements TransportService {

    private static final Logger logger = LoggerFactory.getLogger(SimpleTransportService.class);

    public static final int SOCKET_TIMEOUT = 5000;

    private DatagramSocket clientSocket;

    private InetAddress ipAddress;
    private int port;

    /**
     * Creates new instance of service
     *
     * @param bridgeIp IP address of a bridge
     */
    public SimpleTransportService(String bridgeIp, int port) {
        try {
            this.ipAddress = InetAddress.getByName(bridgeIp);
            this.port = port;
            this.clientSocket = new DatagramSocket();
            this.clientSocket.setBroadcast(true);
            logger.info("Created connection for bridge IP: {}", ipAddress.getHostAddress());
        } catch (UnknownHostException e) {
            throw new MilightException(String.format("Bridge IP address is malformed: ", e));
        } catch (SocketException e) {
            throw new MilightException(String.format("Unable to create UDP socket connection"));
        }
    }

    @Override
    public void sendPackage(byte[] message, Consumer<byte[]> consumer) {
        String requestString = HexUtils.getHexAsString(message);
        try {
            DatagramPacket sendPacket = new DatagramPacket(message, message.length, ipAddress, port);
            logger.debug("Attempting to send request {} to address {}:{}", new Object[]{requestString, ipAddress, port});
            clientSocket.send(sendPacket);
            logger.info("Sent request {} to address {}:{}", new Object[]{requestString, ipAddress, port});
        } catch (Exception e) {
            throw new MilightException(String.format("Unable to send request %s", requestString), e);
        }
        if (consumer != null) {
            try {
                logger.debug("Waiting for response for request {}", requestString);
                byte[] receiveData = new byte[256];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.setSoTimeout(SOCKET_TIMEOUT);
                clientSocket.receive(receivePacket);
                byte[] response = new byte[receivePacket.getLength()];
                for (int i = 0; i < response.length; i++) {
                    response[i] = receivePacket.getData()[i];
                }
                String responseString = HexUtils.getHexAsString(response);
                logger.info("Received response from {}:{} : {}", new Object[]{receivePacket.getAddress(), receivePacket.getPort(), responseString});
                consumer.accept(response);
            } catch (Exception e) {
                throw new MilightException(String.format("Unable to get response for command %s", requestString), e);
            }
        }
    }
}
