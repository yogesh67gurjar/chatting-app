package com.yogeshandroid.mycircle.Fragment.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayoutMediator;
import com.yogeshandroid.mycircle.Adapters.HomeFragmentTabLayoutAdapter;
import com.yogeshandroid.mycircle.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {

    HomeFragmentTabLayoutAdapter adapter;
    FragmentHomeBinding binding;
    private String[] titles={"Camera","Chats","Stories"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // fragment setup
        adapter = new HomeFragmentTabLayoutAdapter(getActivity());
        binding.viewPager.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (((tab, position) -> tab.setText(titles[position])))).attach();
        binding.viewPager.setCurrentItem(1);
        return binding.getRoot();
    }
}