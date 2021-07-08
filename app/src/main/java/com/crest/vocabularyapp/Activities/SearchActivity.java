package com.crest.vocabularyapp.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.crest.vocabularyapp.Adapters.SearchDefinitionAdapter;
import com.crest.vocabularyapp.Adapters.SearchMnemonicAdapter;
import com.crest.vocabularyapp.Models.DatabaseHelper;
import com.crest.vocabularyapp.Models.Word;
import com.crest.vocabularyapp.R;
import com.crest.vocabularyapp.databinding.ActivitySearchBinding;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchActivity extends AppCompatActivity implements SearchMnemonicAdapter.MnemonicClickListener, SearchDefinitionAdapter.DefinitionClickListener {

    ActivitySearchBinding binding;

    private ArrayList<String> text = new ArrayList<>();
    private ArrayList<String> definitionText = new ArrayList<>();

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
            if(status) finish();
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

        if(function.equals("search")) {
            definitionAdapter = new SearchDefinitionAdapter(getApplicationContext(), definitionText, null, true);
            binding.searchDefinitionRecyclerView.setAdapter(definitionAdapter);

            mnemonicAdapter = new SearchMnemonicAdapter(getApplicationContext(), text, null, true);
        }
        else {
            definitionAdapter = new SearchDefinitionAdapter(getApplicationContext(), definitionText, this, false);
            binding.searchDefinitionRecyclerView.setAdapter(definitionAdapter);

            mnemonicAdapter = new SearchMnemonicAdapter(getApplicationContext(), text, this, false);
        }
        binding.searchMnemonicRecyclerView.setAdapter(mnemonicAdapter);


        binding.searchPageSubmit.setOnClickListener(v -> {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());


            executor.execute(new Runnable() {
                @Override
                public void run() {

                    try {
                        text.clear();
                        String word = binding.searchPageText.getText().toString();

                        String baseUrl = "https://mnemonicdictionary.com/word/" + word;

                        String url = baseUrl;

                        Document doc = Jsoup.connect(url).get();

                        Elements data2 = doc.select("div[class=media-body]");
                        Elements data = doc.select("div[class=card-text] p");
                       

                        for (Element indData : data) {
                            if (indData.select("p").text() == "") {
                                continue;
                            }
                            if (indData.select("p").text().equals("Powered by Mnemonic Dictionary")) {
                                continue;
                            }
                            text.add(indData.select("p").text());
                        }


                        for (Element indData2 : data2) {
                            ArrayList<String> breakthis = new ArrayList<>();
                            String[] definitions = indData2.text().split("Definition");
                            for (String definition : definitions) {
                                definitionText.add(definition);
                            }
                        }



                        definitionText.remove(0);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            prevViewDefinition = null;
                            prevPosDefinition = -100;

                            if(!function.equals("search")) {
                                binding.addWordCollectionButton.setVisibility(View.VISIBLE);
                            }

                            binding.placeholderSearchPage.setVisibility(View.GONE);
                            binding.mainSearchContent.setVisibility(View.VISIBLE);
                            definitionAdapter.notifyDataSetChanged();
                            mnemonicAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });

        });

    }

    @Override
    public void onClickDefinition(View v, int position) {

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

        selectedDefinition = definitionText.get(position);

        selectedType = wordType.getText().toString();

        Log.d("DEFINITION", selectedDefinition);
        Log.d("DEFINITIONTYPE", selectedType);

    }

    @Override
    public void onClickMnemonic(View v, int position) {

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
        selectedMnemonic = text.get(position);

        Log.d("MNEMONIC", selectedMnemonic);

    }
}