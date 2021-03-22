package com.intelia.sdk.eligibility.remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Nosa Belvi on 6/23/18.
 * @ApiClient is a singleton class implementation for performing retrofit request
 * @apiClient for making request from any class
 * @provideHttpClient is base httpclient for request
 */


object ApiClient {

    //private const val API_BASE_URL = "http://loanapp-data-engine.dev.intelia.io/"
    private const val API_BASE_URL = "http://15.237.117.167:8008/"


    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            ) // for serialization. Great resource for json parsing
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // for rx. Enable the use of Observable instead of {@link Call}
            .client(signedClient())
            .build()
    }

    fun retrofitSigned(key: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            ) // for serialization. Great resource for json parsing
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // for rx. Enable the use of Observable instead of {@link Call}
            .client(signedClient(key))
            .build()
    }


    private fun signedClient(key: String = ""): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(interceptor)
        if (!key.isEmpty()) {
            client.addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()

                    .addHeader("key", key)
                    .build()
                chain.proceed(newRequest)
            }
        }
        return client.build()
    }


}












