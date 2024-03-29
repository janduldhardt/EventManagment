package com.example.jan.eventmanagment

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.jan.eventmanagment.Extensions.API
import com.example.jan.eventmanagment.Extensions.MyApplication.Companion.context
import com.example.jan.eventmanagment.Extensions.displayUserTime
import com.example.jan.eventmanagment.Extensions.loadCurrentStudentId
import com.example.jan.eventmanagment.Models.Enrollment
import com.example.jan.eventmanagment.Models.EventResponseWithStatus
import com.example.jan.eventmanagment.Models.StudentProfile
import kotlinx.android.synthetic.main.activity_event_info.btn_eventInfo_enrollcancel
import kotlinx.android.synthetic.main.activity_event_info.constraint_layout_eventInfo
import kotlinx.android.synthetic.main.activity_event_info.image_eventInfo_eventImage
import kotlinx.android.synthetic.main.activity_event_info.text_eventInfo_date
import kotlinx.android.synthetic.main.activity_event_info.text_eventInfo_description
import kotlinx.android.synthetic.main.activity_event_info.text_eventInfo_facebook
import kotlinx.android.synthetic.main.activity_event_info.text_eventInfo_line
import kotlinx.android.synthetic.main.activity_event_info.text_eventInfo_phoneNumber
import kotlinx.android.synthetic.main.activity_event_info.text_eventInfo_termsAndConditions
import kotlinx.android.synthetic.main.activity_event_info.text_eventInfo_title
import kotlinx.android.synthetic.main.activity_event_info.text_eventInfo_venue
import kotlinx.android.synthetic.main.event_item_view.view.image_eventImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventInfoActivity : AppCompatActivity() {

    lateinit var client: API
    lateinit var eventid: String
    lateinit var currentStudentId: String
    lateinit var status: String
    var isBusy: Boolean = false
    val mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_info)

        constraint_layout_eventInfo.visibility = View.GONE
        //TODO: Loading Bar
        client = RetrofitService().client

        currentStudentId = loadCurrentStudentId(this)
        eventid = intent.getStringExtra("EXTRA_eventId")

        loadEvent()
    }

    fun onClickEnroll(v: View) {
        if (!isBusy) {
            isBusy = true
            if (btn_eventInfo_enrollcancel.tag == 0) { //ALLOW
                loadProfile()
//                postEnrollment() --> Inside loadProfile Response!!
            } else if (btn_eventInfo_enrollcancel.tag == 1) { //CLOSED
//            TODO: SimpleAlertDialog
            } else { //ENROLLED ALREADY
            val builder = AlertDialog.Builder(this@EventInfoActivity)
                .setTitle("Cancel enrollment?")
                .setMessage("Are you sure you want to cancel the enrollment to this event?")
                .setNegativeButton("Cancel",{ dialogInterface: DialogInterface, i: Int ->
                    isBusy = false
                })
                .setOnDismissListener {
                    isBusy = false
                }
            .setPositiveButton("Confirm", { dialogInterface: DialogInterface, i: Int ->
                cancelEnrollment()
            })
        builder.show()
            }
        }
    }

    private fun loadProfile() {
        val call = client.getStudentProfile(currentStudentId)
        call.enqueue(object : Callback<StudentProfile> {
            override fun onFailure(call: Call<StudentProfile>, t: Throwable) {
                isBusy = false
            }

            override fun onResponse(call: Call<StudentProfile>, response: Response<StudentProfile>) {
                if (response.code() == 404) {
                    val dialog = AlertDialog.Builder(this@EventInfoActivity)
                        .setTitle("Profile not found")
                        .setMessage("Please fill in your profile first")
                        .setPositiveButton("Ok") { dialogInterface: DialogInterface, i: Int ->
                            val intent = Intent(this@EventInfoActivity, ProfileActivity::class.java)
                            startActivity(intent)
                        }
                    dialog.show()
                }else {
                    isBusy = true
                    val builder = AlertDialog.Builder(this@EventInfoActivity)
                        .setTitle("Confirm Enrollment?")
                        .setMessage("Are you sure you want to enroll for this event?")
                        .setNegativeButton("Cancel",{ dialogInterface: DialogInterface, i: Int ->
                            isBusy = false
                        })
                        .setPositiveButton("Confirm", { dialogInterface: DialogInterface, i: Int ->
                            postEnrollment()
                        })

                    builder.show()

                }
            }
        })
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
        isBusy = true
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
        isBusy = true
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
                isBusy = false
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

        val options = RequestOptions()
        options.centerCrop()
        Glide.with(this@EventInfoActivity)
            .load(item.eventDetail.eventImage)
            .apply(options)
            .into(image_eventInfo_eventImage)

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

    fun createDialog(){


    }
}