package com.yogeshandroid.mycircle.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yogeshandroid.mycircle.Adapters.ChatsAdapter;
import com.yogeshandroid.mycircle.R;
import com.yogeshandroid.mycircle.databinding.FragmentChatsBinding;

public class Chats extends Fragment {

    FragmentChatsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(getLayoutInflater());

        binding.chatsRecyclerView.setAdapter(new ChatsAdapter(getContext()));
        binding.chatsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }
}