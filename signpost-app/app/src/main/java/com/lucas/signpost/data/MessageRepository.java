package com.lucas.signpost.data;

import com.lucas.signpost.model.Loc;
import com.lucas.signpost.model.Message;
import com.lucas.signpost.model.Messages;

import java.util.List;
import java.util.function.Consumer;

import io.realm.mongodb.App;

public interface MessageRepository {

    /**
     * Init respository with Realm app
     */
    void init(App app);

    /**
     * get a set of messages with a certain radius of a given latitude/longitude
     * @param loc
     * @param callback
     */
    void getLocalMessages(Loc loc, Consumer<Messages> callback);

    /**
     * Writes message to backend
     * @param message
     */
    void upsert(Message message);

    /**
     * Searches for messages matching the given term
     * @param term
     * @param callback
     */
    void search(String term, Consumer<List<Message>> callback);

}
