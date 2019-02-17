package com.example.jan.eventmanagment

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.jan.eventmanagment.Extensions.API
import com.example.jan.eventmanagment.Extensions.MyApplication.Companion.context
import com.example.jan.eventmanagment.Extensions.displayUserTime
import com.example.jan.eventmanagment.Extensions.loadCurrentStudentId
import com.example.jan.eventmanagment.Models.Enrollment
import com.example.jan.eventmanagment.Models.EventResponseWithStatus
import com.example.jan.eventmanagment.Models.StudentProfile
import kotlinx.android.synthetic.main.activity_event_info.btn_eventInfo_enrollcancel
import kotlinx.android.synthetic.main.activity_event_info.constraint_layout_eventInfo
import kotlinx.android.synthetic.main.activity_event_info.text_eventInfo_date
import kotlinx.android.synthetic.main.activity_event_info.text_eventInfo_description
import kotlinx.android.synthetic.main.activity_event_info.text_eventInfo_facebook
import kotlinx.android.synthetic.main.activity_event_info.text_eventInfo_line
import kotlinx.android.synthetic.main.activity_event_info.text_eventInfo_phoneNumber
import kotlinx.android.synthetic.main.activity_event_info.text_eventInfo_termsAndConditions
import kotlinx.android.synthetic.main.activity_event_info.text_eventInfo_title
import kotlinx.android.synthetic.main.activity_event_info.text_eventInfo_venue
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventInfoActivity : AppCompatActivity() {

    lateinit var client: API

    lateinit var eventid: String

    lateinit var currentStudentId: String

    lateinit var status: String

    var isBusy: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_info)
        constraint_layout_eventInfo.visibility = View.GONE
        //TODO: Loading Bar
        client = RetrofitService().client

        currentStudentId = loadCurrentStudentId(this)
        eventid = intent.getStringExtra("EXTRA_eventId")

        loadEvent()

        btn_eventInfo_enrollcancel.setOnClickListener {
            if (!isBusy) {
                enrollButtonOnClick()
                isBusy = true
            }
        }
    }

    private fun enrollButtonOnClick() {
        if (btn_eventInfo_enrollcancel.tag == 0) { //ALLOW
            postEnrollment()
        } else if (btn_eventInfo_enrollcancel.tag == 1) { //CLOSED
//            TODO: SimpleAlertDialog
        } else { //ENROLLED ALREADY
//            TODO: SimpleAlertDialog
            cancelEnrollment()
        }
    }

    private fun cancelEnrollment() {
        val call = client.cancelEnrollment(currentStudentId, eventid)
        call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_SHORT).show()
                Log.d("cancelEnrollment", t.toString())
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@EventInfoActivity, HomeScreenActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun postEnrollment() {
        val enrollment = Enrollment(currentStudentId, eventid, false, true)
        val call = client.submitEnrollment(enrollment)
        call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_SHORT).show()
                Log.d("enrollButtonOnClick", t.toString())
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@EventInfoActivity, HomeScreenActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun loadEvent() {
        val call = client.getEventInfo(eventid, currentStudentId)
        call.enqueue(object : Callback<EventResponseWithStatus> {
            override fun onFailure(call: Call<EventResponseWithStatus>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(
                call: Call<EventResponseWithStatus>,
                response: Response<EventResponseWithStatus>
            ) {
                constraint_layout_eventInfo.visibility = View.VISIBLE
                val item = response.body()!!
                refreshLayout(item)
            }
        })
    }

    private fun refreshLayout(item: EventResponseWithStatus) {
        text_eventInfo_title.text = item.eventDetail.eventTitle
        text_eventInfo_date.text = displayUserTime(item.eventDetail.eventDateTimeStart)
        text_eventInfo_venue.text = item.eventDetail.eventVenue
        text_eventInfo_description.text = item.eventDetail.eventDescription
        text_eventInfo_termsAndConditions.text = item.eventDetail.eventTermAndCondition
        text_eventInfo_phoneNumber.text = item.eventDetail.eventTelephoneNumber
        text_eventInfo_facebook.text = item.eventDetail.eventFacebook
        text_eventInfo_line.text = item.eventDetail.eventLine

        //Enrollment Button
        status = item.status
        if (status == "ALLOW") {
            btn_eventInfo_enrollcancel.tag = 0
        } else if (status == "CLOSED") {
            btn_eventInfo_enrollcancel.text = "CLOSED"
            btn_eventInfo_enrollcancel.tag = 1
        } else {
            btn_eventInfo_enrollcancel.text = "Cancel Enrollment"
            btn_eventInfo_enrollcancel.tag = 2
        }
    }
}