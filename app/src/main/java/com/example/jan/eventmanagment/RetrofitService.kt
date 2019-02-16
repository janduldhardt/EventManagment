package com.example.jan.eventmanagment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.example.jan.eventmanagment.Extensions.API
import com.example.jan.eventmanagment.Models.Event
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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



