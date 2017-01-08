package com.jeefix.iot.milight.common;

/**
 * Hex utility methods used across project
 * Created by Maciej Iskra (iskramac) on 2017-01-02.
 */
public class HexUtils {

    final protected static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    /**
     * Converts given Hex string argument into byte array representation. Spaces in hex string are ignored
     * Inspired from StackOverflow thread:
     * http://stackoverflow.com/questions/8890174/in-java-how-do-i-convert-a-hex-string-to-a-byte
     * @param messageBytes hex string to convert
     * @return byte[] representation of hex string
     */
    public static byte[] getStringAsHex(String messageBytes) {
        if (messageBytes == null || messageBytes.isEmpty()) {
            return new byte[0];
        }
        messageBytes = messageBytes.replace(" ", "");
        int len = messageBytes.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(messageBytes.charAt(i), 16) << 4)
                    + Character.digit(messageBytes.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * Converts given byte array into space separated String representation.
     * Inspired from StackOverflow thread:
     * http://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
     *
     * @param bytes to convert
     * @return human readable hex representation of given array
     */
    public static String getHexAsString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        char[] hexChars = new char[bytes.length * 3 - 1];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 3] = HEX_ARRAY[v >>> 4];
            hexChars[j * 3 + 1] = HEX_ARRAY[v & 0x0F];
            if (j != bytes.length - 1) hexChars[j * 3 + 2] = ' ';
        }
        return new String(hexChars);
    }
}
