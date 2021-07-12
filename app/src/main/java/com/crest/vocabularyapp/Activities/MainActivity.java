package com.crest.vocabularyapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crest.vocabularyapp.Adapters.MainCollectionAdapter;
import com.crest.vocabularyapp.Models.Collection;
import com.crest.vocabularyapp.Models.DatabaseHelper;
import com.crest.vocabularyapp.Models.Word;
import com.crest.vocabularyapp.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainCollectionAdapter.ItemClickListener {

    ActivityMainBinding binding;
    ArrayList<Collection> collections = new ArrayList<>();

    MainCollectionAdapter adapter;
    DatabaseHelper db = new DatabaseHelper(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DatabaseHelper db = new DatabaseHelper(MainActivity.this);

        binding.searchWordButton.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            i.putExtra("FUNCTION", "search");
            startActivity(i);
        });

        binding.addCollectionButton.setOnClickListener( v -> {
            Intent i = new Intent(MainActivity.this, AddCollectionActivity.class);
            startActivity(i);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.collectionRecyclerView.setLayoutManager(layoutManager);

        collections = db.getCollections();

        adapter = new MainCollectionAdapter(this, collections, this);
        binding.collectionRecyclerView.setAdapter(adapter);

        registerForContextMenu(binding.collectionRecyclerView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        collections = db.getCollections();

        adapter = new MainCollectionAdapter(this, collections, this);
        binding.collectionRecyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(int position) {
        Collection clickedCollection = collections.get(position);
        Intent i = new Intent(MainActivity.this, CollectionPageActivity.class);
        i.putExtra("COLLECTION_NAME", clickedCollection.getName());
        i.putExtra("COLLECTION_ID", clickedCollection.getId());
        startActivity(i);
    }

    @Override
    public void onItemLongClick(int position) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Delete Collection")
                .setMessage("Are you sure you want to delete " + collections.get(position).getName())
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteCollection(collections.get(position).getId());
                        collections = db.getCollections();
                        MainCollectionAdapter adapter = new MainCollectionAdapter(MainActivity.this, collections, MainActivity.this);
                        binding.collectionRecyclerView.setAdapter(adapter);
                    }
                })

                .setNegativeButton("No", null)
                .show();
    }

}