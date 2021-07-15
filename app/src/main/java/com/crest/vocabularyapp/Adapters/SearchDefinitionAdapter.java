package com.crest.vocabularyapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crest.vocabularyapp.Models.Definition;
import com.crest.vocabularyapp.R;
import com.crest.vocabularyapp.databinding.DefinitionItemBinding;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class SearchDefinitionAdapter extends RecyclerView.Adapter<SearchDefinitionAdapter.DefinitionItemViewHolder> {

    private ArrayList<Definition> text;
    private Context context;
    DefinitionClickListener definitionClickListener;
    Boolean isSearch;

    public SearchDefinitionAdapter(Context context, ArrayList<Definition> text, DefinitionClickListener definitionClickListener, Boolean isSearch) {
        this.text = text;
        this.context = context;
        this.isSearch = isSearch;
        this.definitionClickListener = definitionClickListener;
    }

    @Override
    public DefinitionItemViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.definition_item, parent, false);
        return new DefinitionItemViewHolder(view, definitionClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull SearchDefinitionAdapter.DefinitionItemViewHolder holder, int position) {
        Definition current = text.get(position);

        String ans = current.getDefiniton();
        String type = "";


        if (ans.substring(0, 5).equals("(adj)")) {
            type = "Adjective";
            ans = ans.substring(5);
        }
        if (ans.substring(0, 6).equals("(noun)")) {
            type = "Noun";
            ans = ans.substring(6);
        }
        if (ans.substring(0, 6).equals("(verb)")) {
            type = "Verb";
            ans = ans.substring(6);
        }

        holder.binding.definitionText.setText(ans);
        holder.binding.definitionType.setText(type);

        if(isSearch) {
            holder.binding.addDefinitionClickButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return text.size();
    }

    public class DefinitionItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        DefinitionItemBinding binding;
        DefinitionClickListener definitionClickListener;

        public DefinitionItemViewHolder(@NonNull View itemView, DefinitionClickListener definitionClickListener) {
            super(itemView);
            binding = DefinitionItemBinding.bind(itemView);
            this.definitionClickListener = definitionClickListener;

            if(!isSearch) itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            definitionClickListener.onClickDefinition(v, getAdapterPosition(), isSearch);
        }
    }

    ;

    public interface DefinitionClickListener {
        void onClickDefinition(View v, int position, boolean isSearch);
    }

}
