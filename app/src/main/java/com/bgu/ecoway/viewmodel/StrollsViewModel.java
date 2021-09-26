package com.bgu.ecoway.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.bgu.ecoway.adapters.StrollAdapter;
import com.bgu.ecoway.data.Stroll;
import com.bgu.ecoway.repository.SuggestionsRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class StrollsViewModel extends ViewModel {

    private SuggestionsRepository suggestionsRepository;
    private LiveData<List<Stroll>> strolls;

    @Inject
    public StrollsViewModel(SuggestionsRepository suggestionsRepository) {
        this.suggestionsRepository = suggestionsRepository;
        strolls = suggestionsRepository.getStrollSuggestions();
    }

    public LiveData<List<Stroll>> getStrolls() {
        return strolls;
    }
}
