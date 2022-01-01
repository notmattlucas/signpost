package com.lucas.signpost.model;

public class Message {

    private String message;

    private Loc loc;

    public Message(String message, Loc loc) {
        this.message = message;
        this.loc = loc;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Loc getLocation() {
        return loc;
    }

    public void setLocation(Loc loc) {
        this.loc = loc;
    }

}
