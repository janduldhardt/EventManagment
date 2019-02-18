package com.example.jan.eventmanagment.Models

import com.google.gson.annotations.SerializedName

data class EventResponseWithStatus (
    @SerializedName("status") val status : String,
    @SerializedName("eventDetail") val eventDetail : Event
)