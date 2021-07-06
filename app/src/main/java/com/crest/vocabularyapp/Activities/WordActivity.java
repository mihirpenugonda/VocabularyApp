package com.crest.vocabularyapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.crest.vocabularyapp.Models.Word;
import com.crest.vocabularyapp.databinding.ActivityWordBinding;

import java.io.Serializable;

public class WordActivity extends AppCompatActivity {

    ActivityWordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.definitionLayout.addDefinitionClickButton.setVisibility(View.INVISIBLE);
        binding.mnemonicLayout.addMnemonicClickButton.setVisibility(View.INVISIBLE);

        Word word = new Word(getIntent().getStringExtra("WORD"), getIntent().getStringExtra("WORD_DEFINITION"), getIntent().getStringExtra("WORD_MNEMONIC"), getIntent().getStringExtra("WORD_TYPE"));

        binding.definitionLayout.definitionType.setText(word.getType());
        binding.definitionLayout.definitionText.setText(word.getDefinition());

        binding.mnemonicLayout.mnemonicText.setText(word.getMnemonic());

        binding.wordPageText.setText(word.getWordName());

        binding.wordBackClickButton.setOnClickListener( v -> {
            finish();
        });
    }
}