package com.example.jan.eventmanagment

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_event_info.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class EventInfo : AppCompatActivity() {

    lateinit var eventid : String

    lateinit var loggedStudentId : String
    lateinit var retrofit : RetrofitService
    var isEnrolled : Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_info)
        retrofit = RetrofitService()
        retrofit.start(this)


        loggedStudentId = (this.application as currentStudent).getStudent().studentId
        eventid = intent.getStringExtra("EXTRA_eventId")
//        text_eventName.setText(eventid)
        Toast.makeText(this, eventid, Toast.LENGTH_SHORT).show()

        isEnrolledCheck()






        getInfo()

        btn_enroll.setOnClickListener {
            if(isEnrolled) {
                cancelEnrollment()
            } else {
                enroll()
            }
        }
    }

    private fun cancelEnrollment() {
        val retr = RetrofitService()
        retr.start(this)
        val client = retr.client
        val call = client.cancelEnrollment(loggedStudentId,eventid)
        call.enqueue(object : Callback<Void>{
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@EventInfo,t.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Toast.makeText(this@EventInfo,"Sucess", Toast.LENGTH_LONG).show()
                val intent = Intent(this@EventInfo, HomeScreen::class.java)
                startActivity(intent)
            }

        })





    }

    private fun isEnrolledCheck() { // This function checks if the user is already enrolled in this event
            val client = retrofit.client
        val call = client.listUserEvents(loggedStudentId)
        call.enqueue(object : Callback<List<Event>>{
            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                val userevents = response.body()!!
                for (i in 0..userevents.size-1) {
                    if(eventid.toInt() == userevents[i].eventId) {
                        isEnrolled = true
                    }
                }
                if(isEnrolled) {
                    btn_enroll.setText("Cancel enrollment")
                }
            }

        })
    }

    private fun enroll() {
        val builder = AlertDialog.Builder(this@EventInfo)
//        builder.setTitle("")
        builder.setMessage("Are you sure you want to enroll in this event?")
        builder.setPositiveButton("Confirm"){dialog, which ->
            enrollconfirmed()
//            Toast.makeText(this,"Confirmed!!",Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancel"){dialog, which ->
//            Toast.makeText(this,"Canceled!!",Toast.LENGTH_SHORT).show()
        }

        val dialog = builder.create().show()
    }

    private fun enrollconfirmed() {
        //Get the current timestamp
        val sdfDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val now = Date()
        val strDate = sdfDate.format(now)


        val enrollment = Enrollment(loggedStudentId.toInt(), eventid.toInt(), strDate, false, false)

        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
                .baseUrl("http://testevent20181121095158.azurewebsites.net")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        val client : API = retrofit.create(API::class.java)

        val call  = client.createEnrollment(enrollment)

        call.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@EventInfo, t.toString(), Toast.LENGTH_LONG).show()
                text_eventTermsAndConditions.setText(t.toString())
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
               val intent = Intent(this@EventInfo, HomeScreen::class.java)
                startActivity(intent)
            }

        })

    }

    private fun getInfo() {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://testevent20181121095158.azurewebsites.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val client : API = retrofit.create(API::class.java)

        val call  = client.getEventInfo(eventid)

        call.enqueue(object : Callback<Event>{
            override fun onFailure(call: Call<Event>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                Toast.makeText(this@EventInfo,"Sucess", Toast.LENGTH_SHORT).show()
                val eventObject = response.body()
                text_eventName.setText(eventObject?.eventTitle)
//                text_eventDateStart.setText(eventObject?.eventDateTimeStart)
                showTime(eventObject?.eventDateTimeStart)
                text_eventVenue.setText(eventObject?.eventVenue)
                text_eventOrganizer.setText(eventObject?.eventOrganizer)
                text_eventDescription.setText(eventObject?.eventDescription)
                text_eventTermsAndConditions.setText(eventObject?.eventTermAndCondition)


                Glide.with(this@EventInfo)
                        .load(eventObject?.eventImage)
                        .into(image_eventImage)
            }

        }
        )


    }

    private fun showTime(inputString : String?) {
        val fromUser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val myFormat = SimpleDateFormat("E dd'. 'MMM yyyy")
        val reformattedStr = myFormat.format(fromUser.parse(inputString))
        text_eventDateStart.setText(reformattedStr)
    }
}
