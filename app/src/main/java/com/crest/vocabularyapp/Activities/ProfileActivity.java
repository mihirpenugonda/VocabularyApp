package com.crest.vocabularyapp.Activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.crest.vocabularyapp.Models.DatabaseHelper;
import com.crest.vocabularyapp.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DatabaseHelper db = new DatabaseHelper(this);

        binding.profileBack.setOnClickListener(v -> {
            finish();
        });

        binding.profileDeleteAll.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Data")
                    .setMessage("Are You sure you want to delete all Collections.")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.deleteAll();
                        }
                    });
            });

        binding.profileUpdate.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("username", binding.profileUserName.getText().toString());
            myEdit.commit();
        });
    }
}