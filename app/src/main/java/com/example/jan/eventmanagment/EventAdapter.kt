package com.example.jan.eventmanagment

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.jan.eventmanagment.Models.Event
import kotlinx.android.synthetic.main.event_item_view.view.*
import java.text.SimpleDateFormat

class EventAdapter(private val context: Context, val inputList: List<Event>?) :
    RecyclerView.Adapter<EventAdapter.ViewHolder>() {


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): EventAdapter.ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.event_item_view, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (inputList != null) {
            return inputList.size
        }
        return 0
    }

    override fun onBindViewHolder(p0: EventAdapter.ViewHolder, p1: Int) {
        var item = inputList?.get(p1)
        p0.view.text_eventName.text = item?.eventTitle
        p0.view.text_eventLocation.text = item?.eventVenue

        val eventTimes = displayFormatTime(item!!.eventDateTimeStart, item.eventDateTimeEnd)
        p0.view.text_eventDate.text = eventTimes

        if (item.eventAvalibility == null) {
            p0.view.text_eventSeatsLeft.visibility = View.GONE
        } else {
            p0.view.text_eventSeatsLeft.text = item.eventAvalibility.toString() + " SEATS LEFT"
        }

        val options = RequestOptions()
        options.centerCrop()
        Glide.with(context)
            .load(item.eventImage)
            .apply(options)
            .into(p0.view.image_eventImage)

        p0.itemView.setOnClickListener {
            val intent = Intent(context, EventInfoActivity::class.java).apply {
                putExtra("EXTRA_eventId", item.eventId.toString())
            }
            context.startActivity(intent)
        }
    }

    private fun reformatTime(inputString: String?): String {
        val fromServer = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val myFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val reformattedStr = myFormat.format(fromServer.parse(inputString))
        return reformattedStr
    }

    fun displayFormatTime(s1: String, s2: String?): String {
        if (s2.isNullOrEmpty()) {
            return reformatTime(s1)
        } else {

            val sf1 = reformatTime(s1)
            val sf2 = reformatTime(s2)

            val subsf1 = sf1.subSequence(0, 9)
            if (subsf1 in sf2) {
                return sf1 + " - " + sf2.subSequence(sf2.length - 6, sf2.length - 1)
            } else {
                return sf1 + " - " + sf2
            }
        }
    }
}