package com.example.jan.eventmanagment

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.example.jan.eventmanagment.Extensions.loadCurrentStudentId
import com.example.jan.eventmanagment.Extensions.loadCurrentStudentName
import com.example.jan.eventmanagment.Models.Event
import kotlinx.android.synthetic.main.activity_home_screen.*

class HomeScreenActivity : AppCompatActivity() {

    lateinit var currentStudentId: String
    lateinit var currentStudentName: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        findViewById<View>(R.id.nav_home).isClickable = false
        setFragment(Fragment_Home())

        currentStudentId = loadCurrentStudentId(this)
        currentStudentName = loadCurrentStudentName(this)

        text_currentStudentid_header.text = currentStudentId
        text_currentStudentName_header.text = currentStudentName

        main_nav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    setFragment(Fragment_Home())
                    findViewById<View>(R.id.nav_home).isClickable = false
                    findViewById<View>(R.id.nav_EventHome).isClickable = true

                }
                R.id.nav_EventHome -> {
                    setFragment(Fragment_EventHome())
                    findViewById<View>(R.id.nav_EventHome).isClickable = false
                    findViewById<View>(R.id.nav_home).isClickable = true


                }
            }
            true
        }

    }
    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_frame, fragment)
                .commit()
    }
}
