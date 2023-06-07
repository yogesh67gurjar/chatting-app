package com.yogeshandroid.mycircle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.yogeshandroid.mycircle.databinding.ActivityInsideChatBinding;

public class InsideChat extends AppCompatActivity {
    ActivityInsideChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsideChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        binding.toolbar.setNavigationIcon(R.drawable.ic_back);
//
//        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

//        binding.backBtnInsideChat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.inside_chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.call) {
            Toast.makeText(this, "call", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.videoCall) {
            Toast.makeText(this, "videocall", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.block) {
            Toast.makeText(this, "block", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}