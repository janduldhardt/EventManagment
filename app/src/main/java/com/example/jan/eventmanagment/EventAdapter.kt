package com.example.jan.eventmanagment

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_event.view.*



class EventAdapter(private val context : Context, val inputList : List<Event>?) :
        RecyclerView.Adapter<EventAdapter.ViewHolder>() {



        class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)


        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): EventAdapter.ViewHolder {
                val view = LayoutInflater.from(p0.context).inflate(R.layout.fragment_event, p0, false)
                return ViewHolder(view)


        }




    override fun getItemCount(): Int {
            return inputList!!.size

        }

        override fun onBindViewHolder(p0: EventAdapter.ViewHolder, p1: Int) {
            var item = inputList?.get(p1)
            p0.view.text_eventName.setText(item?.eventTitle)

            val options = RequestOptions()
            options.centerCrop()
            Glide.with(context)
                .load(item?.eventImage)
                .apply(options)
                .into(p0.view.image_eventImage)

            p0.itemView.setOnClickListener {
                val intent = Intent(context, EventInfo::class.java).apply {
                    putExtra("EXTRA_eventId", item?.eventId.toString())
                }
                context.startActivity(intent)
            }
        }



}