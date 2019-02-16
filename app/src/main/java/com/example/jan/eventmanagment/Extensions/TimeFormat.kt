package com.example.jan.eventmanagment.Extensions

import java.text.SimpleDateFormat

fun displayUserTime(time : String) : String{

        val fromServer = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val inApp = SimpleDateFormat("MMMM dd. HH:mm")
        val reformat = inApp.format(fromServer.parse(time))
    return reformat

}