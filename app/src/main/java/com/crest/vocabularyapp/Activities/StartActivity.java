package com.crest.vocabularyapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.crest.vocabularyapp.Models.Definition;
import com.crest.vocabularyapp.Models.Mnemonic;
import com.crest.vocabularyapp.databinding.ActivityStartBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = "STARTACT";
    ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.startButton.setOnClickListener(v -> {
            Intent i = new Intent(StartActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }
}