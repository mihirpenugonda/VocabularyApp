package com.crest.vocabularyapp.Adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crest.vocabularyapp.Models.Word;
import com.crest.vocabularyapp.R;
import com.crest.vocabularyapp.databinding.CollectionWordItemBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CollectionWordAdapter extends RecyclerView.Adapter<CollectionWordAdapter.WordViewHolder> {

    String[] data;
    Context context;
    ArrayList<Word> wordList;
    CollectionWordClickListener collectionWordClickListener;

    public CollectionWordAdapter(Context context, ArrayList<Word> wordList, CollectionWordClickListener collectionWordClickListener) {
        this.context = context;
        this.wordList = wordList;
        this.data = data;
        this.collectionWordClickListener = collectionWordClickListener;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.collection_word_item, parent, false);
        return new CollectionWordAdapter.WordViewHolder(view, collectionWordClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CollectionWordAdapter.WordViewHolder holder, int position) {
        Word word = wordList.get(position);

        holder.binding.collectionWordText.setText(word.getWordName());
        holder.binding.collectionWordTextType.setText(word.getType());
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener {

        CollectionWordItemBinding binding;
        CollectionWordClickListener collectionWordClickListener;

        public WordViewHolder(@NonNull @NotNull View itemView, CollectionWordClickListener collectionWordClickListener) {
            super(itemView);
            binding = CollectionWordItemBinding.bind(itemView);
            this.collectionWordClickListener = collectionWordClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            collectionWordClickListener.setClickListener(getAdapterPosition());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(), 101, 0, "Delete");

        }

        @Override
        public boolean onLongClick(View v) {
            collectionWordClickListener.setLongClickListener(getAdapterPosition());
            return true;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public interface CollectionWordClickListener {
        void setClickListener(int position);
        void setLongClickListener(int position);
    }
}
