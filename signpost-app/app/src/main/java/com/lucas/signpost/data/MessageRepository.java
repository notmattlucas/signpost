package com.lucas.signpost.data;

import com.lucas.signpost.model.Loc;
import com.lucas.signpost.model.Messages;

public interface MessageRepository {

    Messages getMessages(Loc loc);

}
