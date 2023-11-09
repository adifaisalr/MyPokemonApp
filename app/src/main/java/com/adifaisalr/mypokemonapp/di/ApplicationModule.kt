package com.adifaisalr.mypokemonapp.di

import com.adifaisalr.core.data.api.Api
import com.adifaisalr.core.data.api.MyCallAdapterFactory
import com.adifaisalr.core.data.api.PokemonService
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
            .addConverterFactory(GsonConverterFactory.create())
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
