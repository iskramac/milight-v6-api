package com.jeefix.iot.milight.transport;

import com.jeefix.iot.milight.common.HexUtils;
import com.jeefix.iot.milight.common.MilightException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.function.Consumer;

/**
 * Handles low level communication with milight bridge.
 * Created by Maciej Iskra (iskramac) on 2017-01-02.
 */
public class MessageTransportService {

    private static final Logger logger = LoggerFactory.getLogger(MessageTransportService.class);

    public static final int SOCKET_TIMEOUT = 5000;

    private static DatagramSocket clientSocket;
    private static InetAddress ipAddress;

    /**
     * Creates new instance of service
     *
     * @param bridgeIp IP address of a bridge
     */
    public MessageTransportService(String bridgeIp) {
        try {
            ipAddress = InetAddress.getByName(bridgeIp);
            clientSocket = new DatagramSocket();
            logger.info("Created connection for bridge IP: {}", ipAddress.getHostAddress());
        } catch (UnknownHostException e) {
            throw new MilightException(String.format("Bridge IP address is malformed: ", e));
        } catch (SocketException e) {
            throw new MilightException(String.format("Unable to create UDP socket connection"));
        }
    }

    /**
     * Sends message to given port
     *
     * @param port     to which send message
     * @param message  content
     * @return response for a message
     */
    public void sendPackage(int port, byte[] message) {
        sendPackage(port, message, null);
    }

    /**
     * Sends message to given port
     *
     * @param port     to which send message
     * @param message  content
     * @param consumer of a response. If not sent response will not be picked up
     * @return response for a message
     */
    public void sendPackage(int port, byte[] message, Consumer<byte[]> consumer) {
        String requestString = HexUtils.getHexAsString(message);
        try {
            DatagramPacket sendPacket = new DatagramPacket(message, message.length, ipAddress, port);
            logger.debug("Attempting to send request {} to address {}:{}", new Object[]{requestString, ipAddress.getHostName(), port});
            clientSocket.send(sendPacket);
            logger.info("Sent request {} to address {}:{}", new Object[]{requestString, ipAddress.getHostName(), port});
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
