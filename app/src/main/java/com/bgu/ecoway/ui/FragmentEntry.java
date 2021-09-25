package com.bgu.ecoway.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bgu.ecoway.MainViewModel;
import com.bgu.ecoway.data.AddressSuggestion;
import com.bgu.ecoway.data.RouteSuggestion;
import com.bgu.ecoway.databinding.FragmentEntryBinding;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FragmentEntry extends Fragment {

    MainViewModel mainViewModel;
    private FragmentEntryBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEntryBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(mainViewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel.getAddressSuggestions().observe(getViewLifecycleOwner(), this::updateDataInAddressSuggestionsAdapter);
        mainViewModel.getRouteSuggestions().observe(getViewLifecycleOwner(), this::updateDataInRouteSuggestionsAdapter);

    }

    private void updateDataInAddressSuggestionsAdapter(List<AddressSuggestion> suggestions) {
        mainViewModel.getAddressSuggestionsAdapter().setData(suggestions);
    }

    private void updateDataInRouteSuggestionsAdapter(List<RouteSuggestion> suggestions) {
        mainViewModel.getRouteSuggestionsAdapter().setData(suggestions);
    }

}