package com.yogeshandroid.mycircle.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yogeshandroid.mycircle.R;
import com.yogeshandroid.mycircle.databinding.FragmentCallsBinding;


public class Calls extends Fragment {

    FragmentCallsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCallsBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }
}