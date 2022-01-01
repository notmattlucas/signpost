package com.lucas.signpost.data;

import com.lucas.signpost.model.Loc;
import com.lucas.signpost.model.Message;
import com.lucas.signpost.model.Messages;

import java.util.ArrayList;
import java.util.List;

public class DummyMessageRepository implements MessageRepository {

    @Override
    public Messages getMessages(Loc loc) {
        List<Message> ms = new ArrayList<>();
        ms.add(new Message(
                "Hello World!",
                new Loc(51.65171598274609, -3.241569174443418)
        ));
        ms.add(new Message(
                "Round the bend",
                new Loc(51.65184786645307, -3.241410253562531)
        ));
        ms.add(new Message(
                "Here be Dragon Way",
                new Loc(51.65156995333375, -3.242145849378498)
        ));
        return new Messages(ms);
    }

}
