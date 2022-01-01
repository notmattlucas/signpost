package com.lucas.signpost.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import androidx.databinding.Bindable;

public class Messages {

    private List<Message> messages;

    public Messages(List<Message> messages) {
        this.messages = messages;
    }

    public Messages() {
        this.messages = new ArrayList<>();
    }

    public void forEach(Consumer<Message> action) {
        messages.forEach(action);
    }

}
