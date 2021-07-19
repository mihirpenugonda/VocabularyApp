package com.crest.vocabularyapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements MainCollectionAdapter.ItemClickListener {

    ActivityMainBinding binding;
    ArrayList<Collection> collections = new ArrayList<>();

    MainCollectionAdapter adapter;
    DatabaseHelper db = new DatabaseHelper(MainActivity.this);

    boolean isEmpty = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("userData", this.MODE_PRIVATE);
        binding.mainActivityUser.setText("Hello, " + sharedPreferences.getString("username", "Mihir"));

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

        binding.profileButton.setOnClickListener( v -> {
            Intent i = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(i);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.collectionRecyclerView.setLayoutManager(layoutManager);

        collections.clear();
        collections = db.getCollections();

        if(collections.size() == 0) {
            collections.add(new Collection("No Collections Available", -1));
            isEmpty = true;
        }

        adapter = new MainCollectionAdapter(this, collections, this, isEmpty);
        binding.collectionRecyclerView.setAdapter(adapter);

        registerForContextMenu(binding.collectionRecyclerView);

    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("userData", this.MODE_PRIVATE);
        binding.mainActivityUser.setText("Hello, " + sharedPreferences.getString("username", "Mihir"));

        collections.clear();
        collections = db.getCollections();

        isEmpty = false;

        if(collections.size() == 0) {
            collections.add(new Collection("No Collections Available", -1));
            isEmpty = true;
        }

        adapter = new MainCollectionAdapter(this, collections, this, isEmpty);
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
                        isEmpty = false;
                        db.deleteCollection(collections.get(position).getId());
                        collections = db.getCollections();
                        MainCollectionAdapter adapter = new MainCollectionAdapter(MainActivity.this, collections, MainActivity.this, isEmpty);
                        binding.collectionRecyclerView.setAdapter(adapter);
                    }
                })

                .setNegativeButton("No", null)
                .show();
    }

}