package com.adifaisalr.mypokemonapp.di

import com.adifaisalr.core.data.api.PokemonApi
import com.adifaisalr.core.data.api.PokemonService
import com.adifaisalr.core.data.api.RetrofitPokemonApi
import com.adifaisalr.core.data.repository.PokemonRepositoryImpl
import com.adifaisalr.core.domain.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideNetworkDataSource(service: PokemonService): PokemonApi {
        return RetrofitPokemonApi(service)
    }

    @Provides
    @Singleton
    fun provideRepository(pokemonApi: PokemonApi): PokemonRepository {
        return PokemonRepositoryImpl(pokemonApi)
    }
}