package com.example.jan.eventmanagment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_fragment__event_home.*
import kotlinx.android.synthetic.main.fragment_fragment__home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class Fragment_Home : Fragment() {

    lateinit var loggedStudentId : String
    lateinit var currentStudentEvents : List<Event>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        loggedStudentId = (activity as HomeScreen).loggedStudentId


        loadStudentsEvents()



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment__home, container, false)
    }




    private fun loadStudentsEvents() {

        val retrofit = RetrofitService()
        retrofit.start(context!!)
        val client = retrofit.client
        val call = client.listUserEvents(loggedStudentId)
        call.enqueue(object : Callback<List<Event>>{
            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                currentStudentEvents  = response.body()!!
                RecyclerView_home.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    adapter = EventAdapter(context, currentStudentEvents)
                }
            }

        })

 }


}
