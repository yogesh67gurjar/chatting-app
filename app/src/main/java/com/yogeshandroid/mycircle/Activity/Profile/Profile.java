package com.yogeshandroid.mycircle.Activity.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yogeshandroid.mycircle.R;
import com.yogeshandroid.mycircle.databinding.ActivityProfileBinding;

public class Profile extends AppCompatActivity {
ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}