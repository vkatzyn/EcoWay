package com.bgu.ecoway.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bgu.ecoway.viewmodel.MainViewModel;
import com.bgu.ecoway.R;
import com.bgu.ecoway.data.RouteSuggestion;
import com.bgu.ecoway.databinding.RouteSuggestionItemBinding;

public class RouteSuggestionsAdapter extends BaseAdapter<RouteSuggestion, RouteSuggestionItemBinding> {

    MainViewModel mainViewModel;

    public RouteSuggestionsAdapter(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_suggestion_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.ViewHolder<RouteSuggestion, RouteSuggestionItemBinding> holder, int position) {
        super.onBindViewHolder(holder, position);
        RouteSuggestion item = data.get(position);
        ((ViewHolder) holder).bindVariables(item, mainViewModel);

    }

    static class ViewHolder extends BaseAdapter.ViewHolder<RouteSuggestion, RouteSuggestionItemBinding> {

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setVariables(RouteSuggestion item) {
            binding.setRouteSuggestion(item);
        }

        public void bindVariables(RouteSuggestion item, MainViewModel viewModel) {
            super.bindVariables(item);
            binding.setViewModel(viewModel);
        }
    }
}
