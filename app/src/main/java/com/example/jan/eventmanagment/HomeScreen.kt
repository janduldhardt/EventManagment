package com.example.jan.eventmanagment

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_home_screen.*
import kotlinx.android.synthetic.main.fragment_fragment__event_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeScreen : AppCompatActivity() {

    lateinit var loggedStudent : Student
    lateinit var loggedStudentId : String
    lateinit var currentStudentEvents : List<Event>
    lateinit var retrofit : RetrofitService
    lateinit var eventList : List<Event>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        findViewById<View>(R.id.nav_home).isClickable = false




        loggedStudent = (this.application as currentStudent).getStudent()

        loggedStudentId = loggedStudent.studentId

        text_currentStudentid_header.setText(loggedStudentId)
        text_currentStudentName_header.setText(loggedStudent.firstName + " " + loggedStudent.lastName)

        retrofit = RetrofitService()
        retrofit.start(this)

        getUsersEventList()






        main_nav.setOnNavigationItemSelectedListener {item ->
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
        val call = retrofit.client.listUserEvents(loggedStudentId)
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


    private fun setFragment(fragment : Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_frame, fragment)
                .commit()
    }
}
