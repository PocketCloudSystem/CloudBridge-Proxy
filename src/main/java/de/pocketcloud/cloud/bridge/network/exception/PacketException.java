package de.pocketcloud.cloud.bridge.network.exception;

/**
 * Exception thrown when packet operations fail
 */
public class PacketException extends Exception {
    
    public PacketException(String message) {
        super(message);
    }
    
    public PacketException(String message, Throwable cause) {
        super(message, cause);
    }
}
