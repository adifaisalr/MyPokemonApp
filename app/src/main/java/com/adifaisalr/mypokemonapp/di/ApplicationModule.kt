package com.adifaisalr.mypokemonapp.di

import com.adifaisalr.core.data.api.Api
import com.adifaisalr.core.data.api.MyCallAdapterFactory
import com.adifaisalr.core.data.api.PokemonService
import com.adifaisalr.core.domain.model.ApiResource
import com.adifaisalr.core.domain.model.NamedApiResource
import com.adifaisalr.core.domain.model.util.ApiResourceAdapter
import com.adifaisalr.core.domain.model.util.NamedApiResourceAdapter
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Singleton
    @Provides
    fun provideService(): PokemonService {
        return Retrofit.Builder()
            .baseUrl(Api.DEFAULT_BASE_URL)
            .client(Api.getDefaultClient())
            .addCallAdapterFactory(MyCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder().apply {
                    setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    registerTypeAdapter(
                        TypeToken.get(ApiResource::class.java).type,
                        ApiResourceAdapter()
                    )
                    registerTypeAdapter(
                        TypeToken.get(NamedApiResource::class.java).type,
                        NamedApiResourceAdapter()
                    )
                }.create()
            ))
            .build()
            .create(PokemonService::class.java)
    }

//    @Singleton
//    @Provides
//    fun provideDB(app: Application): CurrencyExchangeDb {
//        return Room
//            .databaseBuilder(app, CurrencyExchangeDb::class.java, "currency_exchange.db")
//            .build()
//    }
//
//    @Singleton
//    @Provides
//    fun provideDao(db: CurrencyExchangeDb): CurrencyExchangeDao {
//        return db.dao()
//    }
}
