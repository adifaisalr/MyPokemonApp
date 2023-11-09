package com.adifaisalr.mypokemonapp

import com.facebook.soloader.SoLoader
import okhttp3.OkHttpClient

class DebugMyPokemonApp : MyPokemonApp() {

    override fun onCreate() {
        SoLoader.init(this, false)
        FlipperWrapper.setup(this)
        super.onCreate()
    }

    override fun httpClientBuilder(): OkHttpClient.Builder {
        return super.httpClientBuilder()
            .addNetworkInterceptor(FlipperWrapper.flipperNetworkInterceptor)
    }
}