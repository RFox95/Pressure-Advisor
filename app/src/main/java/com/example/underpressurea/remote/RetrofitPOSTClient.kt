package com.example.underpressurea.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitPOSTClient {

    private var retrofit: Retrofit? = null

    val okHttpClient = OkHttpClient()

    fun getClient(baseUrl1:String):Retrofit{
        if(retrofit ==null){
            retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl1)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}