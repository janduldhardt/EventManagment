package com.example.jan.eventmanagment.Extensions

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import com.example.jan.eventmanagment.LoginActivity


fun loadCurrentStudentId(context : Context) : String {
    val saved_values = PreferenceManager.getDefaultSharedPreferences(context)
    return saved_values.getString("currentStudentId", "Student not found")!!
}

fun loadCurrentStudentName(context : Context) : String {
    val saved_values = PreferenceManager.getDefaultSharedPreferences(context)
    return saved_values.getString("currentStudentName", "Student not found")!!
}