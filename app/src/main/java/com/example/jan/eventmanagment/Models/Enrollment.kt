package com.example.jan.eventmanagment.Models

data class Enrollment(val studentId : Int, val eventId : Int, val enrollDateTime : String, val hasPaid : Boolean, val isCheckedIn : Boolean)