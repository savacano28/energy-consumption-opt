package fr.ifpen.synergreen.domain;

/**
 * Created by IFPEN on 21/11/2018.
 */
public class SynergreenException extends RuntimeException {
    public SynergreenException() {
    }

    public SynergreenException(String message) {
        super(message);
    }

    public SynergreenException(String message, Throwable cause) {
        super(message, cause);
    }
}
