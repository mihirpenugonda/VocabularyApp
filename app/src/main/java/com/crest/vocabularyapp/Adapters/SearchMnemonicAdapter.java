package com.crest.vocabularyapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crest.vocabularyapp.Models.Mnemonic;
import com.crest.vocabularyapp.R;
import com.crest.vocabularyapp.databinding.MnemonicItemBinding;

import java.util.ArrayList;

public class SearchMnemonicAdapter extends RecyclerView.Adapter<SearchMnemonicAdapter.MnemonicItemViewHolder>{

    private ArrayList<Mnemonic> text = new ArrayList<>();
    private Context context;
    MnemonicClickListener mnemonicClickListener;
    Boolean isSearch;

    public SearchMnemonicAdapter(Context context, ArrayList<Mnemonic> text, MnemonicClickListener mnemonicClickListener, Boolean isSearch) {
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
        Mnemonic current = text.get(position);

        String mnemonic = current.getMnemonic();

        holder.binding.mnemonicText.setText(current.getMnemonic());
        holder.binding.noOfDislikes.setText(String.valueOf(current.getDislike()));
        holder.binding.noOfLikes.setText(String.valueOf(current.getLikes()) );

        if(isSearch) {
            holder.binding.addMnemonicClickButton.setVisibility(View.GONE);
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
            if(!isSearch) itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mnemonicClickListener.onClickMnemonic(v, getAdapterPosition(), isSearch);
        }
    };

    public interface MnemonicClickListener {
        void onClickMnemonic(View v, int position, boolean isSearch);
    }
}
