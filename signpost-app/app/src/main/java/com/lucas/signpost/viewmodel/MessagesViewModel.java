package com.lucas.signpost.viewmodel;

import com.lucas.signpost.data.MessageRepository;
import com.lucas.signpost.model.Loc;
import com.lucas.signpost.model.Message;
import com.lucas.signpost.model.Messages;

import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MessagesViewModel extends ViewModel {

    private MessageRepository messageRepository;

    private MutableLiveData<Messages> messages;

    private MutableLiveData<Loc> position;

    @Inject
    public MessagesViewModel(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
        this.messages = new MutableLiveData<>(new Messages());
        this.position = new MutableLiveData<>(null);
    }

    public void update(Loc loc) {
        if (moved(loc)) {
            this.messageRepository.getLocalMessages(loc, messages -> {
                this.messages.setValue(messages);
            });
            this.position.setValue(loc);
        }
    }

    public void addMessage(String data, Loc loc) {
        Messages messages = this.messages.getValue();
        Message message = new Message(data, loc);
        messages.add(message);
        message.setOwner(true);
        this.messageRepository.upsert(message);
        this.messages.setValue(messages);
    }

    private boolean moved(Loc loc) {
        return !loc.equals(this.position.getValue());
    }

    public MutableLiveData<Messages> getMessages() {
        return messages;
    }

    public MutableLiveData<Loc> getPosition() {
        return position;
    }

    public void search(String query, Consumer<List<Message>> cbk) {
        messageRepository.search(query, cbk);
    }
}
