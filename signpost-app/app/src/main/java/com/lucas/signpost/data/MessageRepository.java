package com.lucas.signpost.data;

import com.lucas.signpost.model.Loc;
import com.lucas.signpost.model.Message;
import com.lucas.signpost.model.Messages;

import java.util.List;
import java.util.function.Consumer;

import io.realm.mongodb.App;

public interface MessageRepository {

    void init(App app);

    void getLocalMessages(Loc loc, Consumer<Messages> callback);

    void upsert(Message message);

    void search(String term, Consumer<List<Message>> callback);

}
