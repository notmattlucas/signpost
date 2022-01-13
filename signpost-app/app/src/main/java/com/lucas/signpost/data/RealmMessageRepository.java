package com.lucas.signpost.data;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.signpost.model.Loc;
import com.lucas.signpost.model.Message;
import com.lucas.signpost.model.Messages;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import androidx.annotation.NonNull;
import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.functions.Functions;

public class RealmMessageRepository implements MessageRepository {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private App app;

    private String user;

    public void init(App app) {
        this.app = app;
        this.user = app.currentUser().getId();
    }

    @Override
    public void getLocalMessages(Loc loc, Consumer<Messages> cbk) {
        Functions functions = getFunctions();
        functions.callFunctionAsync("getLocalMessages", Arrays.asList(loc.getLatitude(), loc.getLongitude()), List.class, result -> {
            if (result.isSuccess()) {
                List<Message> messages = convert(result);
                cbk.accept(new Messages(messages));
                return;
            }
            Log.w("FUNC", "Could not get messages ", result.getError());
        });
    }

    @Override
    public void upsert(Message message) {
        message.setId(new ObjectId().toString());
        message.setUser(user);
        Functions functions = getFunctions();
        String json;
        try {
            json = MAPPER.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            Log.w("FUNC", "Failed to write message JSON", e);
            return;
        }
        functions.callFunctionAsync("upsertMessage", Arrays.asList(json), Boolean.class, result -> {
            if (!result.isSuccess()) {
                Log.w("FUNC", "Failed to upsert message", result.getError());
            }
        });
    }

    @Override
    public void search(String term, Consumer<List<Message>> cbk) {
        Functions functions = getFunctions();
        functions.callFunctionAsync("search", Arrays.asList(term), List.class, result -> {
            if (result.isSuccess()) {
                List<Message> messages = convert(result);
                cbk.accept(messages);
                return;
            }
            Log.w("FUNC", "Could not get messages ", result.getError());
        });
    }

    @NonNull
    private List<Message> convert(App.Result<List> result) {
        List<Message> messages = new ArrayList<>();
        for (Document document : (List<Document>) result.get()) {
            try {
                String json = document.toJson();
                Message message = MAPPER.readValue(json, Message.class);
                if (owns(message)) {
                    message.setOwner(true);
                }
                messages.add(message);
            } catch (JsonProcessingException e) {
                Log.w("FUNC", "Failed to read message JSON", e);
            }
        }
        return messages;
    }

    private boolean owns(Message message) {
        return this.user.equals(message.getUser());
    }

    private Functions getFunctions() {
        User user = app.currentUser();
        return app.getFunctions(user);
    }

}
