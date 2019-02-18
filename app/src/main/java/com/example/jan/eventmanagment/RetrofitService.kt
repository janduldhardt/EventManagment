package com.example.jan.eventmanagment

import com.example.jan.eventmanagment.Extensions.API
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {

    var client: API

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://testevent20181121095158.azurewebsites.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        client = retrofit.create(API::class.java)
    }
}



