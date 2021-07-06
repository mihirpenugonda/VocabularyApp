package com.crest.vocabularyapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.crest.vocabularyapp.Models.Collection;
import com.crest.vocabularyapp.Models.DatabaseHelper;
import com.crest.vocabularyapp.databinding.ActivityAddCollectionBinding;

public class AddCollectionActivity extends AppCompatActivity {

    ActivityAddCollectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addCollectionBack.setOnClickListener( v -> {
            finish();
        });

        DatabaseHelper dbHelper = new DatabaseHelper(AddCollectionActivity.this);

        binding.addCollectionSubmit.setOnClickListener( v -> {

            String collectionName = binding.addCollectionName.getText().toString();
            int collectionNoOfWords = 0;

            Collection newCollection = new Collection(collectionName, collectionNoOfWords);

            boolean b = dbHelper.addCollection(newCollection);

            if(b == true) {
                finish();
            }
            else {
                Log.d("HERE I AM", String.valueOf(b));
            }
        });
    }
}