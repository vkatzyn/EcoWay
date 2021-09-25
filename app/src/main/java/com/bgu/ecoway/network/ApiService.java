package com.bgu.ecoway.network;

import androidx.lifecycle.LiveData;

import com.bgu.ecoway.data.AddressSuggestion;
import com.bgu.ecoway.data.RouteSuggestion;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET(NetworkConstants.ADDRESS_SUGGESTIONS_URL)
    Call<LiveData<List<AddressSuggestion>>> getAddressSuggestions();

    @GET(NetworkConstants.ROUTE_SUGGESTIONS_URL)
    Call<LiveData<List<RouteSuggestion>>> getRouteSuggestions();
}
