package com.crest.vocabularyapp.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.crest.vocabularyapp.Adapters.SearchDefinitionAdapter;
import com.crest.vocabularyapp.Adapters.SearchMnemonicAdapter;
import com.crest.vocabularyapp.Models.DatabaseHelper;
import com.crest.vocabularyapp.Models.Definition;
import com.crest.vocabularyapp.Models.Mnemonic;
import com.crest.vocabularyapp.Models.Word;
import com.crest.vocabularyapp.R;
import com.crest.vocabularyapp.databinding.ActivitySearchBinding;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.content.ContentValues.TAG;

public class SearchActivity extends AppCompatActivity implements SearchMnemonicAdapter.MnemonicClickListener, SearchDefinitionAdapter.DefinitionClickListener {

    ActivitySearchBinding binding;

    ArrayList<Definition> definitions = new ArrayList<>();
    ArrayList<Mnemonic> mnemonics = new ArrayList<>();

    View prevViewDefinition = null;
    int prevPosDefinition = -100;

    View prevViewMnemonic = null;
    int getPrevPosMnemonic = -100;

    String selectedDefinition = "";
    String selectedMnemonic = "";
    String selectedType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backClickButton.setOnClickListener(v -> {
            finish();
        });

        SearchDefinitionAdapter definitionAdapter;
        SearchMnemonicAdapter mnemonicAdapter;

        String function = getIntent().getStringExtra("FUNCTION");
        int collectionId = getIntent().getIntExtra("COLLECTION_ID", -1);

        DatabaseHelper db = new DatabaseHelper(this);


        binding.addWordCollectionButton.setOnClickListener(v -> {
            Word addWord = new Word();
            addWord.setType(selectedType);
            addWord.setWordName(binding.searchPageText.getText().toString());
            addWord.setMnemonic(selectedMnemonic);
            addWord.setDefinition(selectedDefinition);
            addWord.setCollectionId(collectionId);

            Boolean status = db.addWordToCollection(addWord);
            if (status) finish();
            else Log.d("FUCKING WORK", "WORKKK");
        });


        // Old Layout Managers
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        binding.searchMnemonicRecyclerView.setLayoutManager(layoutManager);
//        binding.searchDefinitionRecyclerView.setLayoutManager(layoutManager2);


        // Google FlexLayoutManager Implementation for Definition
        FlexboxLayoutManager definitionLayoutManager = new FlexboxLayoutManager(getApplicationContext());
        definitionLayoutManager.setFlexDirection(FlexDirection.ROW);
        definitionLayoutManager.setJustifyContent(JustifyContent.CENTER);
        binding.searchDefinitionRecyclerView.setLayoutManager(definitionLayoutManager);

        // Google FlexLayoutManager Implementation for Mnemonic
        FlexboxLayoutManager mnemonicLayoutManager = new FlexboxLayoutManager(getApplicationContext());
        mnemonicLayoutManager.setFlexDirection(FlexDirection.ROW);
        mnemonicLayoutManager.setJustifyContent(JustifyContent.CENTER);
        binding.searchMnemonicRecyclerView.setLayoutManager(mnemonicLayoutManager);

        if (function.equals("search")) {
            definitionAdapter = new SearchDefinitionAdapter(getApplicationContext(), definitions, this, true);
            binding.searchDefinitionRecyclerView.setAdapter(definitionAdapter);

            mnemonicAdapter = new SearchMnemonicAdapter(getApplicationContext(), mnemonics, this, true);
            binding.searchMnemonicRecyclerView.setAdapter(mnemonicAdapter);
        } else {
            definitionAdapter = new SearchDefinitionAdapter(getApplicationContext(), definitions, this, false);
            binding.searchDefinitionRecyclerView.setAdapter(definitionAdapter);

            mnemonicAdapter = new SearchMnemonicAdapter(getApplicationContext(), mnemonics, this, false);
            binding.searchMnemonicRecyclerView.setAdapter(mnemonicAdapter);
        }

        binding.searchPageSubmit.setOnClickListener(v -> {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            definitions.clear();
            mnemonics.clear();

            String word = binding.searchPageText.getText().toString();

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    RequestQueue requestQueue = Volley.newRequestQueue(SearchActivity.this);
                    String url = "http://192.168.56.1:3000/search/" + word;

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                        try {
                            JSONArray definitionsText = response.getJSONArray("definitions");
                            JSONArray mnemonicsText = response.getJSONArray("mnemonics");

                            for (int i = 0; i < definitionsText.length(); i++) {
                                String definition = definitionsText.get(i).toString();
                                definitions.add(new Definition(definition));
                            }

                            for (int i = 0; i < mnemonicsText.length(); i++) {
                                JSONObject mnemonicObject = mnemonicsText.getJSONObject(i);

                                String mnemonic = mnemonicObject.getString("mnemonic");
                                int mnemonicLikes = mnemonicObject.getInt("likes");
                                int mnemonicDislikes = mnemonicObject.getInt("dislikes");

                                if(!mnemonic.equals(""))
                                    mnemonics.add(new Mnemonic(mnemonic, mnemonicLikes, mnemonicDislikes));
                            }
                            definitionAdapter.notifyDataSetChanged();
                            mnemonicAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                        Log.d("HERE", error.toString());
                    });

                    requestQueue.add(jsonObjectRequest);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            prevViewDefinition = null;
                            prevPosDefinition = -100;

                            if (!function.equals("search")) {
                                binding.addWordCollectionButton.setVisibility(View.VISIBLE);
                            }

                            binding.placeholderSearchPage.setVisibility(View.GONE);
                            binding.mainSearchContent.setVisibility(View.VISIBLE);
                        }
                    });

                }
            });
        });
    }

    @Override
    public void onClickDefinition(View v, int position, boolean isSearch) {
        if (!isSearch) {
            if (prevViewDefinition == null && prevPosDefinition == -100) {
                v.findViewById(R.id.add_definition_click_button).setVisibility(View.INVISIBLE);
                prevViewDefinition = v;
                prevPosDefinition = position;
            }
            if (position != prevPosDefinition && v != prevViewDefinition) {
                prevViewDefinition.findViewById(R.id.add_definition_click_button).setVisibility(View.VISIBLE);
                v.findViewById(R.id.add_definition_click_button).setVisibility(View.INVISIBLE);
                prevViewDefinition = v;
                prevPosDefinition = position;
            }

            TextView wordType;
            wordType = v.findViewById(R.id.definition_type);

            selectedDefinition = definitions.get(position).getDefiniton();
            selectedType = wordType.getText().toString();
        }
    }

    @Override
    public void onClickMnemonic(View v, int position, boolean isSearch) {
        if (!isSearch) {
            if (prevViewMnemonic == null && getPrevPosMnemonic == -100) {
                v.findViewById(R.id.add_mnemonic_click_button).setVisibility(View.INVISIBLE);
                prevViewMnemonic = v;
                getPrevPosMnemonic = position;
            }
            if (position != getPrevPosMnemonic && v != prevViewMnemonic) {
                prevViewMnemonic.findViewById(R.id.add_mnemonic_click_button).setVisibility(View.VISIBLE);
                v.findViewById(R.id.add_mnemonic_click_button).setVisibility(View.INVISIBLE);
                prevViewMnemonic = v;
                getPrevPosMnemonic = position;
            }
            selectedMnemonic = mnemonics.get(position).getMnemonic();
        }
    }
}