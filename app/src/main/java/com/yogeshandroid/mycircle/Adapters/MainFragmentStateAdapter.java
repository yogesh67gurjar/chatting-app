package com.yogeshandroid.mycircle.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.yogeshandroid.mycircle.Fragments.Calls;
import com.yogeshandroid.mycircle.Fragments.Chats;
import com.yogeshandroid.mycircle.Fragments.Stories;

public class MainFragmentStateAdapter extends FragmentStateAdapter {

    private String[] titles={"Chats","Status","Calls"};

    public MainFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new Chats();
            case 1:
                return new Stories();
            case 2:
                return new Calls();
        }
        return new Chats();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
