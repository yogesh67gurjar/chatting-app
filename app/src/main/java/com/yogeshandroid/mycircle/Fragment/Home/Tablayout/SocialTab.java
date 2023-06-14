package com.yogeshandroid.mycircle.Fragment.Home.Tablayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.yogeshandroid.mycircle.databinding.FragmentMaintabBinding;


public class SocialTab extends Fragment {

    FragmentMaintabBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMaintabBinding.inflate(inflater, container, false);

        binding.RvSocial.setLayoutManager(new LinearLayoutManager(getContext()));
//        binding.RvSocial.setAdapter(new SocialAdapter(getContext()));


        return binding.getRoot();
    }
}