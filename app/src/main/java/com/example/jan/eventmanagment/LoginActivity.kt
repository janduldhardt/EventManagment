package com.example.jan.eventmanagment

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import com.example.jan.eventmanagment.Models.Student
import kotlinx.android.synthetic.main.activity_home_screen.student_header
import kotlinx.android.synthetic.main.activity_login.btn_login
import kotlinx.android.synthetic.main.activity_login.btn_studentOne
import kotlinx.android.synthetic.main.activity_login.btn_studentThree
import kotlinx.android.synthetic.main.activity_login.btn_studentTwo
import kotlinx.android.synthetic.main.activity_login.btn_time
import kotlinx.android.synthetic.main.activity_login.text_currentStudent
import kotlinx.android.synthetic.main.activity_login.text_time
import java.text.SimpleDateFormat
import java.util.Date

class LoginActivity : AppCompatActivity() {

    lateinit var currentStudentId : String
    lateinit var currentStudentName : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val userOne = Student("1", "Jan", "Duldhardt", "VMS")
        val userTwo = Student("2", "Peter", "Meier", "VME")
        val userThree = Student("6113187", "Sanpawat", "Yong", "VMS")

        btn_studentOne.setOnClickListener {
            currentStudentId = userOne.studentId
            currentStudentName = "${userOne.firstName} ${userOne.lastName}"
            refresh()
        }

        btn_studentTwo.setOnClickListener {
            currentStudentId = userTwo.studentId
            currentStudentName = "${userTwo.firstName} ${userTwo.lastName}"
            refresh()

        }


        btn_studentThree.setOnClickListener {
            currentStudentId = userThree.studentId
            currentStudentName = "${userThree.firstName} ${userThree.lastName}"
            refresh()
        }


        btn_login.setOnClickListener{
            val intent = Intent(this@LoginActivity, TodayPageActivity::class.java)
            startActivity(intent)
        }

        btn_time.setOnClickListener {
            val sdfDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val now = Date()
            val strDate = sdfDate.format(now)
            text_time.text = strDate
        }


    }
    fun refresh() {
        val saved_values = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val editor = saved_values.edit()
        editor.putString("currentStudentId", currentStudentId)
        editor.putString("currentStudentName", currentStudentName)
        editor.commit()
        text_currentStudent.text = currentStudentId


    }

}


