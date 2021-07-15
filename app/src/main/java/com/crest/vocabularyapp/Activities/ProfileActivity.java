package com.crest.vocabularyapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.crest.vocabularyapp.Models.DatabaseHelper;
import com.crest.vocabularyapp.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.profileBack.setOnClickListener( v -> {
            finish();
        });

        binding.profileDeleteAll.setOnClickListener( v -> {
            DatabaseHelper db = new DatabaseHelper(this);
            db.deleteAll();
        });

        binding.profileUpdate.setOnClickListener( v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("username", binding.profileUserName.getText().toString());
            myEdit.commit();
        });
    }
}