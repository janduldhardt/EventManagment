package com.example.jan.eventmanagment

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.jan.eventmanagment.Extensions.API
import com.example.jan.eventmanagment.Models.PostEvent
import com.google.gson.GsonBuilder
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
import kotlinx.android.synthetic.main.activity_event_preview.preview_title
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EventPreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_info)


        // Here we assign all variables with a String of its corrosponding the EditText
        // If the User didnt enter then the val will be set to null

        val pTitle = intent.getStringExtra("pTitle")
        val pStartDate = intent.getStringExtra("pStartDate")
        val pEndDate = checkNullOrString("pEndDate") //Can be null
        val pVenue = intent.getStringExtra("pVenue")
        val pImageUrl = intent.getStringExtra("pImageUrl")
        val pOrganizer = intent.getStringExtra("pOrganizer")
        val pDescription = intent.getStringExtra("pDescription")
        val pTermsAndConditions = checkNullOrString("pTermsAndConditions") //Can be null
        val pTelephoneNumber = intent.getStringExtra("pTelephoneNumber")
        val pFacebook = checkNullOrString("pFacebook") //Can be null
        val pLine = checkNullOrString("pLine") //Can be null
        val pDeadline = checkNullOrString("pDeadline") //Can be null
        val pCapacity = checkNullOrString("pCapacity") //Can be null
        val pTicketPrice = checkNullOrString("pTicketPrice")  //if no entry price is equal to zero

        text_eventInfo_title.text = pTitle
        text_eventInfo_venue.text = pVenue
        text_eventInfo_date.text = pStartDate
        text_eventInfo_description.text = pDescription
        text_eventInfo_termsAndConditions.text = pTermsAndConditions
        text_eventInfo_phoneNumber.text = pTelephoneNumber
        text_eventInfo_line.text = pLine
        text_eventInfo_facebook.text = pFacebook
        btn_eventInfo_enrollcancel.text = "Create Event"

        val options = RequestOptions()
        options.centerCrop()
        Glide.with(this@EventPreviewActivity)
                .load(pImageUrl)
                .apply(options)
                .into(image_eventInfo_eventImage)

        constraint_layout_eventInfo.visibility = View.VISIBLE



        btn_eventInfo_enrollcancel.setOnClickListener {

            val newEvent = PostEvent(pTitle, pStartDate, pEndDate, pVenue, pImageUrl,
                    pOrganizer, pDescription, pTermsAndConditions, pTelephoneNumber,
                    pFacebook, pLine, pDeadline, pCapacity?.toInt(), pCapacity?.toInt(), pTicketPrice!!.toInt())

            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder()
                    .baseUrl("http://testevent20181121095158.azurewebsites.net")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

            val client: API = retrofit.create(API::class.java)

            val call = client.createEvent(newEvent)
            call.enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
//                    Toast.makeText(this@EventPreviewActivity, t.toString(), Toast.LENGTH_LONG).show()
                    preview_title.text = t.message
                }

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    val intent = Intent(this@EventPreviewActivity, HomeScreenActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this@EventPreviewActivity, "Success", Toast.LENGTH_LONG).show()

                }

            })

        }
    }


    //This function checks if the putExtra is an empty String and if yes will change the value to null
    // so when the event is created it the parameter is null and will not be send
    //If the user doesnt enter a ticket price the price will automaticly set to 0
    fun checkNullOrString(stringName: String): String? {
        val stringS = intent.getStringExtra(stringName)
        if (stringName.equals("pTicketPrice") && stringS.isNullOrEmpty()) {
            return "0"
        }
        if (!stringS.isNullOrEmpty()) {
            return stringS
        } else {
            return null
        }
    }

}
