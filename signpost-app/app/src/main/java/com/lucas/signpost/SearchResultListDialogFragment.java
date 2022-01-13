package com.lucas.signpost;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lucas.signpost.databinding.SearchResultItemBinding;
import com.lucas.signpost.databinding.SearchResultsBinding;
import com.lucas.signpost.model.Loc;
import com.lucas.signpost.model.Message;
import com.lucas.signpost.viewmodel.MessagesViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     SearchResultListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class SearchResultListDialogFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_MESSAGES = "messages";

    private SearchResultsBinding binding;

    private List<Message> messages;

    private MessagesViewModel model;

    public SearchResultListDialogFragment(List<Message> messages, MessagesViewModel model) {
        this.messages = messages;
        this.model = model;
    }

    // TODO: Customize parameters
    public static SearchResultListDialogFragment newInstance(List<Message> messages, MessagesViewModel model) {
        final SearchResultListDialogFragment fragment = new SearchResultListDialogFragment(messages, model);
        final Bundle args = new Bundle();
        List<String> hits = messages.stream()
                .map(Message::getMessage)
                .collect(Collectors.toList());
        args.putStringArrayList(ARG_MESSAGES, new ArrayList<>(hits));
        fragment.setArguments(args);
        return fragment;
    }

    private void onSearchChoice(int position) {
        Message message = this.messages.get(position);
        Loc location = message.getLocation();
        model.update(location);
        this.dismiss();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = SearchResultsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new SearchResultAdapter(getArguments().getStringArrayList(ARG_MESSAGES)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView text;

        ViewHolder(SearchResultItemBinding binding) {
            super(binding.getRoot());
            text = binding.text;
        }

    }

    private class SearchResultAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final List<String> messages;

        SearchResultAdapter(List<String> messages) {
            this.messages = messages;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(SearchResultItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.text.setText(Html.fromHtml(messages.get(position)));
            holder.itemView.setOnClickListener(view -> {
                onSearchChoice(position);
            });
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

    }
}