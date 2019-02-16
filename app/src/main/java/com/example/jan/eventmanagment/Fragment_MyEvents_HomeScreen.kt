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
import com.example.jan.eventmanagment.Extensions.loadCurrentStudentId
import com.example.jan.eventmanagment.Models.Event
import kotlinx.android.synthetic.main.activity_home_screen.*
import kotlinx.android.synthetic.main.fragment_fragment__event_home.*
import kotlinx.android.synthetic.main.fragment_fragment__home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class Fragment_Home : Fragment() {

    lateinit var currentStudentId: String
    var currentUserEvents: List<Event>? = null
    lateinit var client: API

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as HomeScreenActivity).loadingPanel.visibility = View.VISIBLE
        currentStudentId = loadCurrentStudentId(context!!)
        client = RetrofitService().client
        loadUserEvents()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment__home, container, false)
    }

    private fun loadUserEvents() {
        val call = client.getEventsByStudentId(currentStudentId)
        call.enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                (activity as HomeScreenActivity).loadingPanel.visibility = View.GONE
                val events: List<Event>? = response.body()
                RecyclerView_myEvents.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    adapter = EventAdapter(context, events)
                }
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                Log.d("loadUserEvents", t.toString())
                Toast.makeText(context, "Error loading Data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
