package com.lucas.signpost.di;

import com.lucas.signpost.data.DummyMessageRepository;
import com.lucas.signpost.data.MessageRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MessagesModule {

    @Provides
    @Singleton
    public MessageRepository messageRepository() {
        return new DummyMessageRepository();
    }

}
