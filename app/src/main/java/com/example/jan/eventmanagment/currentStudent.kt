package com.example.jan.eventmanagment

import android.app.Application
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class currentStudent : Application() {
    lateinit var currentStudent : Student

    fun setcuStudent(s1 : Student) {
        this.currentStudent = s1
    }

    fun getStudent() : Student{
        return currentStudent
    }

}