package com.lucas.signpost.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lucas.signpost.R;
import com.lucas.signpost.model.Loc;
import com.lucas.signpost.viewmodel.MessagesViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WriteMessageFragment extends BottomSheetDialogFragment {

    private final MessagesViewModel model;

    private final Loc location;

    public WriteMessageFragment(MessagesViewModel model, Loc location) {
        this.model = model;
        this.location = location;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.write_message_dialog, container, false);
        Button btn = (Button) view.findViewById(R.id.submit);
        EditText txt = (EditText) view.findViewById(R.id.message);

        btn.setOnClickListener(v -> {
            String message = txt.getText().toString();
            this.model.addMessage(message, location);
            this.dismiss();
        });

        return view;
    }


}
