package com.example.jan.eventmanagment

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Login : AppCompatActivity() {

    lateinit var currentid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val userOne = Student("1", "Jan", "Duldhardt", "VMS")
        val userTwo = Student("2", "Peter", "Meier", "VME")
        val userThree = Student("3", "Ben", "Seifert", "BBA")

        btn_studentOne.setOnClickListener {
            (this.application as currentStudent).setStudent(userOne)
            refresh()
        }

        btn_studentTwo.setOnClickListener {
            (this.application as currentStudent).setStudent(userTwo)
            refresh()

        }


        btn_studentThree.setOnClickListener {
            (this.application as currentStudent).setStudent(userThree)
            refresh()
        }


        btn_login.setOnClickListener{
            val intent = Intent(this@Login, HomeScreen::class.java)
            startActivity(intent)
        }

        btn_time.setOnClickListener {
            val sdfDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val now = Date()
            val strDate = sdfDate.format(now)
            text_time.setText(strDate)
        }


    }
    fun refresh() {
        var currentS = (this.application as currentStudent).getStudent()
        currentid = currentS.studentId
        text_currentStudent.setText(currentid)
    }

}


