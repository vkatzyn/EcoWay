package com.bgu.ecoway.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bgu.ecoway.adapters.AddressSuggestionsAdapter;
import com.bgu.ecoway.adapters.RouteSuggestionsAdapter;
import com.bgu.ecoway.data.AddressSuggestion;
import com.bgu.ecoway.data.RouteSuggestion;
import com.bgu.ecoway.repository.SuggestionsRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private final SuggestionsRepository suggestionsRepository;
    private final AddressSuggestionsAdapter addressSuggestionsAdapter;
    private final RouteSuggestionsAdapter routeSuggestionsAdapter;
    private LiveData<List<AddressSuggestion>> addressSuggestions;
    private LiveData<List<RouteSuggestion>> routeSuggestions;
    private MutableLiveData<RouteSuggestion> currentRouteSuggestion = new MutableLiveData<>();

    @Inject
    public MainViewModel(SuggestionsRepository suggestionsRepository) {
        this.suggestionsRepository = suggestionsRepository;
        addressSuggestionsAdapter = new AddressSuggestionsAdapter(this);
        routeSuggestionsAdapter = new RouteSuggestionsAdapter(this);
        addressSuggestions = suggestionsRepository.getAddressSuggestions();
        routeSuggestions = suggestionsRepository.getRouteSuggestions();
    }

    public void setCurrentRouteSuggestion(RouteSuggestion routeSuggestion) {
        currentRouteSuggestion.setValue(routeSuggestion);
    }

    public MutableLiveData<RouteSuggestion> getCurrentRouteSuggestion() {
        return currentRouteSuggestion;
    }

    public AddressSuggestionsAdapter getAddressSuggestionsAdapter() {
        return addressSuggestionsAdapter;
    }

    public RouteSuggestionsAdapter getRouteSuggestionsAdapter() {
        return routeSuggestionsAdapter;
    }

    public LiveData<List<AddressSuggestion>> getAddressSuggestions() {
        return addressSuggestions;
    }

    public LiveData<List<RouteSuggestion>> getRouteSuggestions() {
        return routeSuggestions;
    }
}
