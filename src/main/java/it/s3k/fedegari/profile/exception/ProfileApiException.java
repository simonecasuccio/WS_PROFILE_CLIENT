package it.s3k.fedegari.profile.exception;

public class ProfileApiException extends RuntimeException {
    private final int statusCode;
    private final String responseBody;
    
    public ProfileApiException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public String getResponseBody() {
        return responseBody;
    }
}