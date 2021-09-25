package com.bgu.ecoway.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bgu.ecoway.data.AddressSuggestion;
import com.bgu.ecoway.data.RouteSuggestion;
import com.bgu.ecoway.network.ApiService;

import java.util.ArrayList;
import java.util.List;

public class SuggestionsRepository {

    ApiService apiService;

    public SuggestionsRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<List<AddressSuggestion>> getAddressSuggestions() {
        MutableLiveData<List<AddressSuggestion>> addressSuggestions = new MutableLiveData<>();
//        apiService.getAddressSuggestions().enqueue(new Callback<LiveData<List<AddressSuggestion>>>() {
//            @Override
//            public void onResponse(Call<LiveData<List<AddressSuggestion>>> call, Response<LiveData<List<AddressSuggestion>>> response) {
//                addressSuggestions.setValue(response.body().getValue());
//            }
//
//            @Override
//            public void onFailure(Call<LiveData<List<AddressSuggestion>>> call, Throwable t) {
//
//            }
//        });
        List<AddressSuggestion> list = new ArrayList<>();
        list.add(new AddressSuggestion("Южнобутовская улица 29 к3", "Москва", "253 м"));
        list.add(new AddressSuggestion("Южнобутовская улица 30 к3", "Москва", "253 м"));
        list.add(new AddressSuggestion("Южнобутовская улица 31 к3", "Москва", "253 м"));
        list.add(new AddressSuggestion("Южнобутовская улица 32 к3", "Москва", "253 м"));
        addressSuggestions.setValue(list);
        return addressSuggestions;
    }

    public LiveData<List<RouteSuggestion>> getRouteSuggestions() {
        MutableLiveData<List<RouteSuggestion>> routeSuggestions = new MutableLiveData<>();
//        apiService.getRouteSuggestions().enqueue(new Callback<LiveData<List<RouteSuggestion>>>() {
//            @Override
//            public void onResponse(Call<LiveData<List<RouteSuggestion>>> call, Response<LiveData<List<RouteSuggestion>>> response) {
//                routeSuggestions.setValue(response.body().getValue());
//            }
//
//            @Override
//            public void onFailure(Call<LiveData<List<RouteSuggestion>>> call, Throwable t) {
//
//            }
//        });
        List<RouteSuggestion> list = new ArrayList<>();
        list.add(new RouteSuggestion("42 мин", "12 км", "100%"));
        list.add(new RouteSuggestion("35 мин", "7 км", "95%"));
        list.add(new RouteSuggestion("10 мин", "2 км", "10%"));
        list.add(new RouteSuggestion("125 мин", "30 км", "55%"));
        list.add(new RouteSuggestion("42 мин", "12 км", "100%"));
        list.add(new RouteSuggestion("35 мин", "7 км", "95%"));
        list.add(new RouteSuggestion("10 мин", "2 км", "10%"));
        list.add(new RouteSuggestion("125 мин", "30 км", "55%"));
        routeSuggestions.setValue(list);
        return routeSuggestions;
    }
}
