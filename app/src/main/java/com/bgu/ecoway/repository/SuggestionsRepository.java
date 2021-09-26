package com.bgu.ecoway.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bgu.ecoway.data.AddressSuggestion;
import com.bgu.ecoway.data.RouteSuggestion;
import com.bgu.ecoway.data.Stroll;
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
        list.add(new RouteSuggestion("1", "42 мин", "12 км", "100%"));
        list.add(new RouteSuggestion("2", "35 мин", "7 км", "95%"));
        list.add(new RouteSuggestion("3", "10 мин", "2 км", "10%"));
        list.add(new RouteSuggestion("4", "125 мин", "30 км", "55%"));
        list.add(new RouteSuggestion("5", "42 мин", "12 км", "100%"));
        list.add(new RouteSuggestion("6", "35 мин", "7 км", "95%"));
        list.add(new RouteSuggestion("7", "10 мин", "2 км", "10%"));
        list.add(new RouteSuggestion("8", "125 мин", "30 км", "55%"));
        routeSuggestions.setValue(list);
        return routeSuggestions;
    }

    public LiveData<List<Stroll>> getStrollSuggestions() {
        MutableLiveData<List<Stroll>> strollSuggestions = new MutableLiveData<>();
        //todo api call
        List<Stroll> list = new ArrayList<>();
        list.add(new Stroll("Воробьевы горы", "Вдоль Москвы-реки в ландшафтном парке. Рядом Лужники, МГУ и канатная дорога.", null, "3.24 км", "2.5 ч", "93%"));
        list.add(new Stroll("По бульварному кольцу", "Прогулка вокруг сердца Москвы. Увидите все сталинские высотки, а также несколько раз пересечете Москву реку.", null, "6.36 км", "4.5 ч", "53%"));
        strollSuggestions.setValue(list);
        return strollSuggestions;
    }
}
