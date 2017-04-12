package com.jeefix.limitlessled.transport;

import java.util.function.Consumer;

/**
 * Handles low level communication with limitlessled bridge.
 * <p>
 * Created by Maciej Iskra (emacisk) on 2017-04-12.
 */

public interface TransportService {

    /**
     * Sends message to given port
     *
     * @param message content
     * @return response for a message
     */
    default void sendPackage(byte[] message) {
        sendPackage(message, null);
    }

    /**
     * Sends message to given port
     *
     * @param message  content
     * @param consumer of a response. If not sent response will not be picked up
     * @return response for a message
     */
    public void sendPackage(byte[] message, Consumer<byte[]> consumer);
}
