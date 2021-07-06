package com.crest.vocabularyapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crest.vocabularyapp.R;
import com.crest.vocabularyapp.databinding.MnemonicItemBinding;

import java.util.ArrayList;

public class SearchMnemonicAdapter extends RecyclerView.Adapter<SearchMnemonicAdapter.MnemonicItemViewHolder>{

    private ArrayList<String> text = new ArrayList<>();
    private Context context;
    MnemonicClickListener mnemonicClickListener;
    Boolean isSearch;

    public SearchMnemonicAdapter(Context context, ArrayList<String> text, MnemonicClickListener mnemonicClickListener, Boolean isSearch) {
        this.text = text;
        this.context = context;
        this.mnemonicClickListener = mnemonicClickListener;
        this.isSearch = isSearch;
    }

    @Override
    public MnemonicItemViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mnemonic_item, parent, false);
        return new MnemonicItemViewHolder(view, mnemonicClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull SearchMnemonicAdapter.MnemonicItemViewHolder holder, int position) {
        String ans = text.get(position);

        holder.binding.mnemonicText.setText(ans);

        if(isSearch) {
            holder.binding.addMnemonicClickButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return text.size();
    }

    public class MnemonicItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        MnemonicItemBinding binding;
        MnemonicClickListener mnemonicClickListener;

        public MnemonicItemViewHolder(@NonNull View itemView, MnemonicClickListener mnemonicClickListener) {
            super(itemView);
            binding = MnemonicItemBinding.bind(itemView);
            this.mnemonicClickListener = mnemonicClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mnemonicClickListener.onClickMnemonic(v, getAdapterPosition());
        }
    };

    public interface MnemonicClickListener {
        void onClickMnemonic(View v, int position);
    }
}
