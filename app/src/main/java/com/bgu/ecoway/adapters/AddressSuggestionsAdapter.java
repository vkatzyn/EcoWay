package com.bgu.ecoway.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bgu.ecoway.viewmodel.MainViewModel;
import com.bgu.ecoway.R;
import com.bgu.ecoway.data.AddressSuggestion;
import com.bgu.ecoway.databinding.AddressSuggestionItemBinding;

public class AddressSuggestionsAdapter extends BaseAdapter<AddressSuggestion, AddressSuggestionItemBinding> {

    MainViewModel mainViewModel;

    public AddressSuggestionsAdapter(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_suggestion_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.ViewHolder<AddressSuggestion, AddressSuggestionItemBinding> holder, int position) {
        super.onBindViewHolder(holder, position);
        AddressSuggestion item = data.get(position);
        ((ViewHolder) holder).bindVariables(item, mainViewModel);
    }

    static class ViewHolder extends BaseAdapter.ViewHolder<AddressSuggestion, AddressSuggestionItemBinding> {

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setVariables(AddressSuggestion item) {
            binding.setAddressSuggestion(item);
        }

        public void bindVariables(AddressSuggestion item, MainViewModel viewModel) {
            super.bindVariables(item);
            binding.setViewModel(viewModel);
        }
    }
}
