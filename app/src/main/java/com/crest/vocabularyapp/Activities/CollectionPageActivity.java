package com.crest.vocabularyapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.crest.vocabularyapp.Adapters.CollectionWordAdapter;
import com.crest.vocabularyapp.Models.DatabaseHelper;
import com.crest.vocabularyapp.Models.Word;
import com.crest.vocabularyapp.databinding.ActivityCollectionPageBinding;

import java.util.ArrayList;

public class CollectionPageActivity extends AppCompatActivity implements CollectionWordAdapter.CollectionWordClickListener {

    ActivityCollectionPageBinding binding;
    String[] data = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48"};
    ArrayList<Word> collectionWord;
    int collectionId;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCollectionPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = new DatabaseHelper(this);

        String collectionName = getIntent().getStringExtra("COLLECTION_NAME");
        collectionId = getIntent().getIntExtra("COLLECTION_ID", -1);
        binding.collectionPageName.setText(collectionName);

        binding.collectionPageBack.setOnClickListener(v -> {
            finish();
        });

        binding.collectionPageAddWord.setOnClickListener(v -> {
            Intent i = new Intent(CollectionPageActivity.this, SearchActivity.class);
            i.putExtra("FUNCTION", "find");
            i.putExtra("COLLECTION_ID", collectionId);
            startActivity(i);
        });

        try {
            collectionWord = db.getCollectionsWord(collectionId);
            Log.d("LENGTH", String.valueOf(collectionWord.size()));
        }
        catch (Exception e) {

        }


        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.collectionWordRecyclerView.setLayoutManager(layoutManager);
        CollectionWordAdapter adapter = new CollectionWordAdapter(this, collectionWord, this);
        binding.collectionWordRecyclerView.setAdapter(adapter);

    }

    @Override
    public void setClickListener(int position) {
        Word word = collectionWord.get(position);

        Intent i = new Intent(this, WordActivity.class);
        i.putExtra("WORD", word.getWordName());
        i.putExtra("WORD_TYPE", word.getType());
        i.putExtra("WORD_DEFINITION", word.getDefinition());
        i.putExtra("WORD_MNEMONIC", word.getMnemonic());
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();

        collectionWord = db.getCollectionsWord(collectionId);
        CollectionWordAdapter adapter = new CollectionWordAdapter(this, collectionWord, this);
        binding.collectionWordRecyclerView.setAdapter(adapter);

    }
}