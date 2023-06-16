package com.yogeshandroid.mycircle.Fragment.Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yogeshandroid.mycircle.Adapters.ChatsAdapter;
import com.yogeshandroid.mycircle.Adapters.SearchAdapter;
import com.yogeshandroid.mycircle.Modal.User;
import com.yogeshandroid.mycircle.R;
import com.yogeshandroid.mycircle.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    FragmentSearchBinding binding;
    FirebaseDatabase database;
    List<User> userList;
    SearchAdapter searchAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();
        userList = new ArrayList<>();
        searchAdapter = new SearchAdapter(getContext(), userList);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    User single = snap.getValue(User.class);
                    single.setUserId(snap.getKey());
                    if (!FirebaseAuth.getInstance().getUid().equals(single.getUserId()))
                    {
                        userList.add(single);
                    }

                }
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        binding.recyclerViewSearch.setAdapter(searchAdapter);
        binding.recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText);
                return false;
            }
        });



        return binding.getRoot();
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<User> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (User item : userList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getUserName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            searchAdapter.filterList(filteredlist);
        }
    }

}