package com.example.jan.eventmanagment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.jan.eventmanagment.Extensions.API
import com.example.jan.eventmanagment.Models.Event
import kotlinx.android.synthetic.main.activity_home_screen.*
import kotlinx.android.synthetic.main.fragment_fragment__event_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class Fragment_EventHome : Fragment() {

    lateinit var allEventsList: List<Event>
    lateinit var client: API

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as HomeScreenActivity).loadingPanel.visibility = View.VISIBLE
        client = RetrofitService().client
        loadAllEvents()
        return inflater.inflate(R.layout.fragment_fragment__event_home, container, false)
    }

    private fun loadAllEvents() {
        val call = client.listAllEvents()
        call.enqueue(object : Callback<List<Event>> {
            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                Log.d("loadAllEvents", t.toString())
                Toast.makeText(context, "Error loading Data", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                (activity as HomeScreenActivity).loadingPanel.visibility = View.GONE
                val events: List<Event>? = response.body()
                RecyclerView_allEvents.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    adapter = EventAdapter(context, events)
                }
            }
        })
    }

//    private fun finishedLoading() {
//        (activity as HomeScreenActivity).loadingPanel.setVisibility(View.GONE)
//        RecyclerView_eventHome.apply {
//            setHasFixedSize(true)
//            layoutManager = LinearLayoutManager(context)
//            adapter = EventAdapter(context, allEventsList)
//        }

//    private fun loadAllEvents() {
//        val retrofit = RetrofitService()
//        retrofit.start(context!!)
//        val client = retrofit.client
//
//        val call = client.listAllEvents()
//
//        call.enqueue(object :  Callback<List<Event>>{
//            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
//                (activity as HomeScreenActivity).loadingPanel.setVisibility(View.GONE)
//                val events : List<Event>? = response.body()
//                RecyclerView_eventHome.apply {
//                    setHasFixedSize(true)
//                    layoutManager = LinearLayoutManager(context)
////                    adapter = EventAdapter(context, events)
//
//                }
//            }
//
//        })    }
}

