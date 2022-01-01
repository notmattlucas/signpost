package com.lucas.signpost.viewmodel;

import com.lucas.signpost.data.MessageRepository;
import com.lucas.signpost.model.Loc;
import com.lucas.signpost.model.Messages;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MessagesViewModel extends ViewModel {

    private MessageRepository messageRepository;

    private MutableLiveData<Messages> messages;

    @Inject
    public MessagesViewModel(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
        this.messages = new MutableLiveData<>(new Messages());
    }

    public void update(Loc loc) {
        Messages messages = this.messageRepository.getMessages(loc);
        this.messages.setValue(messages);
    }

    public MutableLiveData<Messages> getMessages() {
        return messages;
    }

}
