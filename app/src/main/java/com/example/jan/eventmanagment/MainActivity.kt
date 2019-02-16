package com.example.jan.eventmanagment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.jan.eventmanagment.Extensions.API
import com.example.jan.eventmanagment.Models.Event
import kotlinx.android.synthetic.main.activity_main.RecyclerView1
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cacheSize = (5 * 1024 * 1024).toLong()
        val myCache = Cache(this.cacheDir, cacheSize)

        val okHttpClient = OkHttpClient.Builder()
                .cache(myCache)
                .addInterceptor { chain ->
                    var request = chain.request()
                    request = if (hasNetwork(this)!!)
                        request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                    else
                        request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
                    chain.proceed(request)
                }
                .build()



        //1. Create Event Class XX
        //2.1 Create custom View XX
        //2. Create RecyclerView XX
        //3. Use fuel library to get the Event

        val retrofit = Retrofit.Builder()
            .baseUrl("http://testevent20181121095158.azurewebsites.net")
            .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
            .build()

        val client : API = retrofit.create(API::class.java)

        val call  = client.listAllEvents()

        call.enqueue(object : Callback<List<Event>> {
            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
//                Toast.makeText(this@MainActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(call: Call<List<Event>>, response: retrofit2.Response<List<Event>>) {
                val events : List<Event>? = response.body()
                RecyclerView1.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = EventAdapter(this@MainActivity, events)
                }


            }
        } )
    }

    fun hasNetwork(context: Context): Boolean? {
        var isConnected: Boolean? = false // Initial Value
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }
}
