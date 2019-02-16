package com.example.jan.eventmanagment

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.jan.eventmanagment.Extensions.API
import com.example.jan.eventmanagment.Extensions.loadCurrentStudentId
import com.example.jan.eventmanagment.Models.Enrollment
import com.example.jan.eventmanagment.Models.Event
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_event_info.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class EventInfoActivity : AppCompatActivity() {

    lateinit var client: API

    lateinit var eventid: String

    lateinit var loggedStudentId: String

    var isEnrolled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_info)
        constraint_layout_eventInfo.visibility = View.GONE
        //TODO: Loading Bar
        client = RetrofitService().client

        loggedStudentId = loadCurrentStudentId(this)
        eventid = intent.getStringExtra("EXTRA_eventId")
        getInfo()
        btn_eventInfo_enrollcancel.setOnClickListener {
            if (isEnrolled) {
                cancelEnrollment()
            } else {
                enroll()
            }
        }
    }

    private fun cancelEnrollment() {
        val builder = AlertDialog.Builder(this@EventInfoActivity)
        builder.setMessage("Are you sure you want to cancel this event?")
        builder.setPositiveButton("Yes") { _, _ ->
            cancelEnrollmentConfirmed()
        }
        builder.setNegativeButton("No") { _, _ ->
        }

        builder.create().show()
    }

    private fun cancelEnrollmentConfirmed() {
        val call = client.cancelEnrollment(loggedStudentId, eventid)
        call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@EventInfoActivity, t.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Toast.makeText(this@EventInfoActivity, "Sucess", Toast.LENGTH_LONG).show()
                val intent = Intent(this@EventInfoActivity, HomeScreenActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun isEnrolledCheck() { // This function checks if the user is already enrolled in this event
        val call = client.getEventsByStudentId(loggedStudentId)
        call.enqueue(object : Callback<List<Event>> {
            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                val userevents = response.body()
                if (userevents == null){
                    isEnrolled = false
                    return
                }
                for (i in 0..userevents.size - 1) {
                    if (eventid.toInt() == userevents[i].eventId) {
                        isEnrolled = true
                    }
                }
                if (isEnrolled) {
                    btn_eventInfo_enrollcancel.setText("Cancel enrollment")
                }
            }
        })
    }

    private fun enroll() {
        val builder = AlertDialog.Builder(this@EventInfoActivity)
//        builder.setTitle("")
        builder.setMessage("Are you sure you want to enroll in this event?")
        builder.setPositiveButton("Confirm") { dialog, which ->
            enrollconfirmed()
//            Toast.makeText(this,"Confirmed!!",Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
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

        val call = client.createEnrollment(enrollment)

        call.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@EventInfoActivity, t.toString(), Toast.LENGTH_LONG).show()
                text_eventInfo_termsAndConditions.setText(t.toString())
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                val intent = Intent(this@EventInfoActivity, HomeScreenActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun getInfo() {
        val call = client.getEventInfo(eventid)

        call.enqueue(object : Callback<Event> {
            override fun onFailure(call: Call<Event>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                isEnrolledCheck()
                Toast.makeText(this@EventInfoActivity, "Sucess", Toast.LENGTH_SHORT).show()
                val eventObject = response.body()
                text_eventInfo_title.setText(eventObject?.eventTitle)
                text_eventInfo_venue.setText(eventObject?.eventVenue)
//                text_eventInfo.setText(eventObject?.eventOrganizer)  //no Organizer
                text_eventInfo_description.setText(eventObject?.eventDescription)
                text_eventInfo_termsAndConditions.setText(eventObject?.eventTermAndCondition)
                text_eventInfo_phoneNumber.setText(eventObject?.eventTelephoneNumber)
                text_eventInfo_line.setText(eventObject?.eventLine)
                text_eventInfo_facebook.setText(eventObject?.eventFacebook)

                val options = RequestOptions()
                options.centerCrop()
                Glide.with(this@EventInfoActivity)
                    .load(eventObject?.eventImage)
                    .apply(options)
                    .into(image_eventInfo_eventImage)

                constraint_layout_eventInfo.setVisibility(View.VISIBLE)
            }
        }
        )
    }

    private fun showTime(inputString: String?) {
        val fromUser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val myFormat = SimpleDateFormat("MMMM dd. HH:mm")
        val reformattedStr = myFormat.format(fromUser.parse(inputString))
        text_eventInfo_date.setText(reformattedStr)
    }
}
