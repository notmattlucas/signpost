package com.lucas.signpost.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A single message at a location
 */
public class Message {

    @JsonProperty("id")
    private String id;

    @JsonProperty("message")
    private String message;

    @JsonProperty("user")
    private String user;

    @JsonProperty("location")
    private Loc location;

    /**
     * Whether the current user owns the message
     */
    @JsonIgnore
    private boolean owner;

    public Message() {}

    public Message(String message, Loc loc) {
        this.message = message;
        this.location = loc;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Loc getLocation() {
        return location;
    }

    public void setLocation(Loc loc) {
        this.location = loc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (this.id == null) {
            this.id = id;
        }
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean owned() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

}
