package com.sap.coordinatedhttprequests.exception;

/**
 * Custom exception to handle failed HTTP requests.
 */
public class HttpRequestFailedException extends RuntimeException {
    private final int statusCode;

    /**
     * Constructs a new HttpRequestFailedException with a status code and message.
     *
     * @param statusCode the unexpected status code
     * @param message    the error message
     */
    public HttpRequestFailedException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * Gets the HTTP status code associated with this exception.
     *
     * @return the HTTP status code
     */
    public int getStatusCode() {
        return statusCode;
    }
}
