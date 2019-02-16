package com.example.jan.eventmanagment

import com.google.gson.annotations.SerializedName

data class Event (
    @SerializedName("eventId") val eventId : Int?,
    @SerializedName("title") val eventTitle : String,
    @SerializedName("dateTimeStart") val eventDateTimeStart : String,
    @SerializedName("dateTimeEnd") val eventDateTimeEnd : String?,
    @SerializedName("venue") val eventVenue : String,
    @SerializedName("image") val eventImage : String,
    @SerializedName("organizer") val eventOrganizer : String,
    @SerializedName("description") val eventDescription : String,
    @SerializedName("termAndCondition") val eventTermAndCondition : String?,
    @SerializedName("telephoneNumber") val eventTelephoneNumber : String,
    @SerializedName("facebook") val eventFacebook : String?,
    @SerializedName("line") val eventLine : String?,
    @SerializedName("deadLine") val eventDeadLine : String?,
    @SerializedName("capacity") val eventCapacity : Int?,
    @SerializedName("avalibility") val eventAvalibility : Int?,
    @SerializedName("ticketPrice") val eventTicketPrice : Int
    )



