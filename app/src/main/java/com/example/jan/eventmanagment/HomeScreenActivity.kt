package com.example.jan.eventmanagment

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.example.jan.eventmanagment.Extensions.loadCurrentStudentId
import com.example.jan.eventmanagment.Extensions.loadCurrentStudentName
import kotlinx.android.synthetic.main.activity_home_screen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeScreen : AppCompatActivity() {

    lateinit var currentStudentId: String
    lateinit var currentStudentName : String
    lateinit var currentStudentEvents: List<Event>
    lateinit var retrofit: RetrofitService
    lateinit var eventList: List<Event>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        findViewById<View>(R.id.nav_home).isClickable = false





        currentStudentId = loadCurrentStudentId(this)
        currentStudentName = loadCurrentStudentName(this)

        text_currentStudentid_header.setText(currentStudentId)
        text_currentStudentName_header.setText(currentStudentName)

        retrofit = RetrofitService()
        retrofit.start(this)

        getUsersEventList()

        image_createEvent.setOnClickListener {
            val intent = Intent(this@HomeScreen, CreateEventActivity::class.java)
            startActivity(intent)
        }

        main_nav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    setFragment(Fragment_Home())
                    findViewById<View>(R.id.nav_home).isClickable = false
                    findViewById<View>(R.id.nav_EventHome).isClickable = true

                }
                R.id.nav_EventHome -> {
                    setFragment(Fragment_EventHome())
                    findViewById<View>(R.id.nav_EventHome).isClickable = false
                    findViewById<View>(R.id.nav_home).isClickable = true


                }
            }
            true
        }

    }

    private fun getUsersEventList() {
        val call = retrofit.client.listUserEvents(currentStudentId)
        call.enqueue(object : Callback<List<Event>> {
            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                println(t.toString())
            }

            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                eventList = response.body()!!
                setFragment(Fragment_Home())
            }

        })
    }


    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_frame, fragment)
                .commit()
    }
}
