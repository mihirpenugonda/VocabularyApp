package com.crest.vocabularyapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crest.vocabularyapp.R;
import com.crest.vocabularyapp.databinding.DefinitionItemBinding;

import java.util.ArrayList;

public class SearchDefinitionAdapter extends RecyclerView.Adapter<SearchDefinitionAdapter.DefinitionItemViewHolder> {

    private ArrayList<String> text;
    private Context context;
    DefinitionClickListener definitionClickListener;
    Boolean isSearch;

    public SearchDefinitionAdapter(Context context, ArrayList<String> text, DefinitionClickListener definitionClickListener, Boolean isSearch) {
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
        String ans = text.get(position);

        String type = "";

        if (ans.substring(0, 6).equals(" (adj)")) {
            type = "Adjective";
            ans = ans.substring(7);
        }
        if (ans.substring(0, 7).equals(" (noun)")) {
            type = "Noun";
            ans = ans.substring(8);
        }
        if (ans.substring(0, 7).equals(" (verb)")) {
            type = "Verb";
            ans = ans.substring(8);
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
            definitionClickListener.onClickDefinition(v, getAdapterPosition());
        }
    }

    ;

    public interface DefinitionClickListener {
        void onClickDefinition(View v, int position);
    }

}
