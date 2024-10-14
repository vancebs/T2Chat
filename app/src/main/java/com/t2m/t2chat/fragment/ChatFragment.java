package com.t2m.t2chat.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.t2m.t2chat.adapter.ConversationAdapter;
import com.t2m.t2chat.agent.Agent;
import com.t2m.t2chat.databinding.FragmentChatBinding;

public abstract class ChatFragment extends Fragment {
    @SuppressWarnings("unused")
    public static final String TAG = ChatFragment.class.getSimpleName();

    protected FragmentChatBinding binding;

    private final Handler mHandler = new Handler();

    private Agent.Conversation mConversation;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSend.setOnClickListener((v) -> onSend());

        mConversation = getAgent().newConversation();
        binding.conversation.setAdapter(new ConversationAdapter(mConversation));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void onSend() {
        String input = binding.inputMessage.getText().toString();
        binding.inputMessage.getText().clear();

        if (input.trim().isEmpty()) {
            return;
        }

        mConversation.talk(input);
    }

    protected abstract Agent getAgent();

}