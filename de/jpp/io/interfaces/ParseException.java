package de.jpp.io.interfaces;

public class ParseException extends Exception {

    private final String msg;
    public ParseException(String msg) {

        this.msg = msg;
    }

    public ParseException() {

        throw new RuntimeException();
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
