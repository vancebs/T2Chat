package com.t2m.t2chat.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.t2m.t2chat.R;
import com.t2m.t2chat.databinding.FragmentBotListBinding;

public class BotListFragment extends Fragment {

    private FragmentBotListBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentBotListBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnCookbookBot.setOnClickListener((v) -> {
            NavHostFragment.findNavController(BotListFragment.this)
                    .navigate(R.id.action_BotListFragment_to_CookbookBotLlama8BChatFragment);
        });
        binding.btnCookbookBot3b.setOnClickListener((v) -> {
            NavHostFragment.findNavController(BotListFragment.this)
                    .navigate(R.id.action_BotListFragment_to_CookbookBotLlama3BChatFragment);
        });
        binding.btnCookbookBotQwen05b.setOnClickListener((v) -> {
            NavHostFragment.findNavController(BotListFragment.this)
                    .navigate(R.id.action_BotListFragment_to_CookbookBotQwen0_5BChatFragment);
        });
        binding.btnCookbookBotQwen15b.setOnClickListener((v) -> {
            NavHostFragment.findNavController(BotListFragment.this)
                    .navigate(R.id.action_BotListFragment_to_CookbookBotQwen1_5BChatFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}