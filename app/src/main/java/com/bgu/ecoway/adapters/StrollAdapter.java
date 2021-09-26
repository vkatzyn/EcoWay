package com.bgu.ecoway.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bgu.ecoway.R;
import com.bgu.ecoway.data.Stroll;
import com.bgu.ecoway.databinding.StrollSuggestionItemBinding;

import java.util.List;

public class StrollAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Stroll> strolls;

    public StrollAdapter(List<Stroll> strolls) {
        this.strolls = strolls;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StrollViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.stroll_suggestion_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((StrollViewHolder) holder).bindData(strolls.get(position));
        if (position == 1) {
            ((StrollViewHolder) holder).binding.img.setImageResource(R.drawable.blvd);
        }
    }

    @Override
    public int getItemCount() {
        return strolls.size();
    }


    public static class StrollViewHolder extends RecyclerView.ViewHolder {
        protected StrollSuggestionItemBinding binding;


        public StrollViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void bindData(Stroll stroll) {
            binding.setStroll(stroll);
        }
    }
}
