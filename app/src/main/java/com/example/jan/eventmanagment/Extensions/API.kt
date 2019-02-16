package com.example.jan.eventmanagment.Extensions

import com.example.jan.eventmanagment.Models.Enrollment
import com.example.jan.eventmanagment.Models.Event
import com.example.jan.eventmanagment.Models.PostEvent
import retrofit2.Call
import retrofit2.http.*

interface API {
    @GET("/event/getall")
    fun listAllEvents(): Call<List<Event>>

//    @GET("/event/geteventbyid/{eventID}")
//    fun getEventInfo(@Path("eventID") eventID: String): Call<Event>

    @GET("/event/geteventbyid/")
    fun getEventInfo(@Query("eventid") eventID: String): Call<Event>

    @GET("/event/geteventsbystudentid/{studentId}")
    fun getEventsByStudentId(@Path("studentId") studentid: String): Call<List<Event>>

    @POST("/event/submitenrollment")
    fun createEnrollment(@Body enroll: Enrollment): Call<String>

    @POST("/event/addevent")
    fun createEvent(@Body event: PostEvent): Call<Void>

    @PUT("/event/cancellation")
    fun cancelEnrollment(
        @Query("studentId") studentid: String,
        @Query("eventId") eventid: String
    ): Call<Void>
}

/* Enrollment /getallenrollments /submitenrollment
"studentId": 2,
"eventId": 7,
"enrollDateTime": "2017-12-08T10:00:00",
"hasPaid": false,
"isCheckedIn": false,
"checkedInStaffId": 6111234,
"student": null,
"event": null
 */