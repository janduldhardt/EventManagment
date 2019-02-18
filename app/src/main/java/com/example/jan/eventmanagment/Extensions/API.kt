package com.example.jan.eventmanagment.Extensions

import com.example.jan.eventmanagment.Models.Enrollment
import com.example.jan.eventmanagment.Models.Event
import com.example.jan.eventmanagment.Models.EventResponseWithStatus
import com.example.jan.eventmanagment.Models.PostEvent
import com.example.jan.eventmanagment.Models.StudentProfile
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface API {
    @GET("/event/getall")
    fun listAllEvents(): Call<List<Event>>

    @GET("/event/GetEventById/")
    fun getEventInfo(@Query("eventId") eventID: String,
                    @Query("studentId") studentid: String): Call<EventResponseWithStatus>

    @GET("/event/geteventsbystudentid/")
    fun getEventsByStudentId(@Query("studentId") studentid: String): Call<List<Event>>

    @POST("/event/submitenrollment")
    fun submitEnrollment(@Body enroll: Enrollment) : Call<Void>

    @POST("/event/addevent")
    fun createEvent(@Body event: PostEvent): Call<Void>

    @PUT("/event/cancellation")
    fun cancelEnrollment(
        @Query("studentId") studentId: String,
        @Query("eventId") eventId: String
    ): Call<Void>

    @POST("/event/addprofile")
    fun addProfile(@Body profile: StudentProfile): Call<Void>

    @GET("/event/getstudentprofile")
    fun getStudentProfile(@Query("studentId") studentId: String) : Call<StudentProfile>

    @GET("/event/getTodayEvents")
    fun getTodayEvents(@Query("studentId") studentId: String) : Call<List<Event>>



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