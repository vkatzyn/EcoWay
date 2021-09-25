package com.bgu.ecoway.di;

import com.bgu.ecoway.network.ApiService;
import com.bgu.ecoway.repository.SuggestionsRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RepositoryModule {

    @Provides
    @Singleton
    public SuggestionsRepository provideSuggestionsRepository(ApiService apiService) {
        return new SuggestionsRepository(apiService);
    }

}
