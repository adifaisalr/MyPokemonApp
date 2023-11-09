package com.adifaisalr.mypokemonapp

import android.app.Application
import com.adifaisalr.core.data.api.Api
import com.adifaisalr.core.data.api.ApiBaseConfigurator
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient

@HiltAndroidApp
open class MyPokemonApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Api.setBaseConfigurator(object : ApiBaseConfigurator {
            override fun newHttpClientBuilder(): OkHttpClient.Builder = httpClientBuilder()
        })
    }

    open fun httpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }
}