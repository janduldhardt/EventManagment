package com.example.jan.eventmanagment

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.jan.eventmanagment.Extensions.EventTodayAdapter
import com.example.jan.eventmanagment.Extensions.loadCurrentStudentId
import com.example.jan.eventmanagment.Extensions.loadCurrentStudentName
import com.example.jan.eventmanagment.Models.Event
import com.example.jan.eventmanagment.R.layout
import kotlinx.android.synthetic.main.activity_today_page.student_header
import kotlinx.android.synthetic.main.activity_today_page.RecyclerView_Today
import kotlinx.android.synthetic.main.activity_today_page.progressbar_todayPage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodayPageActivity : AppCompatActivity() {

    lateinit var currentStudentId: String
    lateinit var currentStudentName: String

    var client = RetrofitService().client

    var isBusy = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today_page)
        currentStudentId = loadCurrentStudentId(this@TodayPageActivity)


        currentStudentId = loadCurrentStudentId(this)
        currentStudentName = loadCurrentStudentName(this)

        student_header.init(currentStudentName, currentStudentId)
        loadUserEvents()
    }

    private fun loadUserEvents() {
        isBusy = true
        val call = client.getTodayEvents(currentStudentId)
        call.enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                progressbar_todayPage.visibility = View.GONE
                val events: List<Event>? = response.body()
                RecyclerView_Today.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    adapter = EventTodayAdapter(
                        context,
                        events,
                        layout.eventtoday_item_view
                    )
                }
                isBusy = false
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                Log.d("loadUserEvents", t.toString())
                Toast.makeText(this@TodayPageActivity, "Error loading Data", Toast.LENGTH_SHORT).show()
                isBusy = false
            }
        })
    }

    fun onClickGoToEventFunction(v: View) {
        if(!isBusy) {
            val intent = Intent(this@TodayPageActivity, HomeScreenActivity::class.java)
            startActivity(intent)
        }
    }
}
