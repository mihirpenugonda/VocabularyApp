package com.crest.vocabularyapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crest.vocabularyapp.Models.Collection;
import com.crest.vocabularyapp.R;
import com.crest.vocabularyapp.databinding.CollectionMainItemBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MainCollectionAdapter extends RecyclerView.Adapter<MainCollectionAdapter.CollectionViewHolder> {

    private ArrayList<Collection> collections;
    private Context context;
    private ItemClickListener mItemClickListener;
    private boolean isEmpty;

    public MainCollectionAdapter(Context context, ArrayList<com.crest.vocabularyapp.Models.Collection> collections, ItemClickListener itemClickListener, boolean isEmpty) {
        this.context = context;
        this.collections = collections;
        this.mItemClickListener = itemClickListener;
        this.isEmpty = isEmpty;
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.collection_main_item, parent, false);
        return new MainCollectionAdapter.CollectionViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MainCollectionAdapter.CollectionViewHolder holder, int position) {
        Collection current = collections.get(position);

        if(current.getNumberOfWords() == -1) {
            holder.binding.collectionNumberText.setVisibility(View.INVISIBLE);
            holder.binding.collectionNumberTextDefinition.setVisibility(View.INVISIBLE);
            holder.binding.collectionClickButton.setVisibility(View.INVISIBLE);
        }
        else {
            holder.binding.collectionNumberText.setText(String.valueOf(current.getNumberOfWords()));
        }

        holder.binding.collectionNameView.setText(current.getName());
    }

    @Override
    public int getItemCount() {
        return collections.size();
    }

    public class CollectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        CollectionMainItemBinding binding;
        ItemClickListener itemClickListener;

        public CollectionViewHolder(@NonNull @NotNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            binding = CollectionMainItemBinding.bind(itemView);
            this.itemClickListener = itemClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            if(isEmpty) {
                itemView.setOnClickListener(null);
                itemView.setOnLongClickListener(null);
            }

        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onItemLongClick(getAdapterPosition());
            return true;
        }

    }

    public interface ItemClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }
}
