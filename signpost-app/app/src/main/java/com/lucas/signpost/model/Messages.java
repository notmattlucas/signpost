package com.lucas.signpost.model;

import java.util.List;
import java.util.function.Consumer;

import io.realm.RealmList;

public class Messages {

    private List<Message> messages;

    public Messages(List<Message> messages) {
        this.messages = messages;
    }

    public Messages() {
        this.messages = new RealmList<>();
    }

    public void forEach(Consumer<Message> action) {
        messages.forEach(action);
    }

    public void add(Message message) {
        messages.add(message);
    }

    public List<Message> get() { return messages; };

}
