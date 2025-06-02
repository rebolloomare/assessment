package omare.com.mx.microservice.exceptions;

public class InvalidTaskException extends RuntimeException {

    public InvalidTaskException(String message) {
        super(message);
    }

}
