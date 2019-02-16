package com.example.jan.eventmanagment

data class PostEvent(val title : String,
                     val dateTimeStart : String,
                     val dateTimeEnd : String?,
                     val venue : String,
                     val image : String,
                     val organizer : String,
                     val description : String,
                     val termAndCondition : String?,
                     val telephoneNumber : String,
                     val facebook : String?,
                     val line : String?,
                     val deadLine : String?,
                     val capacity : Int?,
                     val avalibility : Int?,
                     val ticketPrice : Int)