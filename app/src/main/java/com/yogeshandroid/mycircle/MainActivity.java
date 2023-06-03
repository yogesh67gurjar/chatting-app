package com.yogeshandroid.mycircle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayoutMediator;
import com.yogeshandroid.mycircle.Adapters.MainFragmentStateAdapter;
import com.yogeshandroid.mycircle.Login.LogIn;
import com.yogeshandroid.mycircle.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

//    FirebaseAuth auth;
//    GoogleSignInOptions gso;
//    GoogleSignInClient gsc;

    private String[] titles = {"Chats", "Stories", "Calls"};
    MainFragmentStateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolBar);

        // fragment setup
        adapter = new MainFragmentStateAdapter(this);
        binding.viewPager.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (((tab, position) -> tab.setText(titles[position])))).attach();


//        gso = new GoogleSignInOptions
//                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("1011067988925-p90igopfruf8v167gnrt31bl1c2s1ng5.apps.googleusercontent.com")
//                .requestEmail()
//                .build();

//        gsc = GoogleSignIn.getClient(MainActivity.this, gso);

//        auth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.logout) {
//                  auth.signOut();
//                gsc.signOut();
            startActivity(new Intent(MainActivity.this, LogIn.class));
        } else if (item.getItemId() == R.id.camera) {

            Toast.makeText(this, "Camera clicked", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.search) {

            Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}