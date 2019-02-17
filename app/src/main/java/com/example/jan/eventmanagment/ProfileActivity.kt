package com.example.jan.eventmanagment

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.support.v4.graphics.drawable.DrawableCompat
import android.widget.Button
import android.widget.Toast
import com.example.jan.eventmanagment.Extensions.MyApplication.Companion.context
import com.example.jan.eventmanagment.Extensions.loadCurrentStudentId
import com.example.jan.eventmanagment.Models.StudentProfile
import kotlinx.android.synthetic.main.activity_profile.button_food10_profile
import kotlinx.android.synthetic.main.activity_profile.button_food1_profile
import kotlinx.android.synthetic.main.activity_profile.button_food2_profile
import kotlinx.android.synthetic.main.activity_profile.button_food3_profile
import kotlinx.android.synthetic.main.activity_profile.button_food4_profile
import kotlinx.android.synthetic.main.activity_profile.button_food5_profile
import kotlinx.android.synthetic.main.activity_profile.button_food6_profile
import kotlinx.android.synthetic.main.activity_profile.button_food7_profile
import kotlinx.android.synthetic.main.activity_profile.button_food8_profile
import kotlinx.android.synthetic.main.activity_profile.button_food9_profile
import kotlinx.android.synthetic.main.activity_profile.button_shirt1_profile
import kotlinx.android.synthetic.main.activity_profile.button_shirt2_profile
import kotlinx.android.synthetic.main.activity_profile.button_shirt3_profile
import kotlinx.android.synthetic.main.activity_profile.button_shirt4_profile
import kotlinx.android.synthetic.main.activity_profile.button_shirt5_profile
import kotlinx.android.synthetic.main.activity_profile.button_shirt6_profile
import kotlinx.android.synthetic.main.activity_profile.textedit_medical_condition_profile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    var shirtButtonList : MutableList<Button> = mutableListOf()
    var foodButtonList : MutableList<Button> = mutableListOf()

    var selectedShirtSize : String? = null
    var selectedFood : String? = null

    var client = RetrofitService().client




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        createButtonLists()



    }

    fun onClickSave(v: View){
        if (selectedFood == null && selectedShirtSize == null){
            Toast.makeText(this, "Select Food Type", Toast.LENGTH_SHORT).show()
        } else if (selectedShirtSize == null ){
            Toast.makeText(this, "Select Shirt Size", Toast.LENGTH_SHORT).show()
        } else if (selectedFood == null ){
            Toast.makeText(this, "Select Food Type", Toast.LENGTH_SHORT).show()
        } else {
            var studentId = loadCurrentStudentId(this)
            val medicalCondition = textedit_medical_condition_profile.editText?.text.toString()
            val newProfile = StudentProfile(studentId, selectedShirtSize!!, selectedFood!!, medicalCondition)
            val call = client.addProfile(newProfile)
            call.enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ProfileActivity, HomeScreenActivity::class.java)
                    startActivity(intent)
                }
            })
        }
    }

    fun onClickFood(v : View){
        for (button in foodButtonList){
            var buttonDrawable = button.getBackground()
            buttonDrawable = DrawableCompat.wrap(buttonDrawable)
            //the color is a direct color int and not a color resource
            DrawableCompat.setTint(buttonDrawable, resources.getColor(R.color.lightGrey))
            button.setBackground(buttonDrawable)
        }
        var buttonDrawable = v.getBackground()
        buttonDrawable = DrawableCompat.wrap(buttonDrawable)
        //the color is a direct color int and not a color resource
        DrawableCompat.setTint(buttonDrawable, resources.getColor(R.color.red))
        v.setBackground(buttonDrawable)
        selectedFood = v.tag.toString()
    }


    fun onClickSize(v : View){
        for (button in shirtButtonList){
            var buttonDrawable = button.getBackground()
            buttonDrawable = DrawableCompat.wrap(buttonDrawable)
            //the color is a direct color int and not a color resource
            DrawableCompat.setTint(buttonDrawable, resources.getColor(R.color.lightGrey))
            button.setBackground(buttonDrawable)
        }
        var buttonDrawable = v.getBackground()
        buttonDrawable = DrawableCompat.wrap(buttonDrawable)
        //the color is a direct color int and not a color resource
        DrawableCompat.setTint(buttonDrawable, resources.getColor(R.color.red))
        v.setBackground(buttonDrawable)
        selectedShirtSize = v.tag.toString()
    }


    private fun createButtonLists() {
        shirtButtonList.add(button_shirt1_profile)
        shirtButtonList.add(button_shirt2_profile)
        shirtButtonList.add(button_shirt3_profile)
        shirtButtonList.add(button_shirt4_profile)
        shirtButtonList.add(button_shirt5_profile)
        shirtButtonList.add(button_shirt6_profile)

        foodButtonList.add(button_food1_profile)
        foodButtonList.add(button_food2_profile)
        foodButtonList.add(button_food3_profile)
        foodButtonList.add(button_food4_profile)
        foodButtonList.add(button_food5_profile)
        foodButtonList.add(button_food6_profile)
        foodButtonList.add(button_food7_profile)
        foodButtonList.add(button_food8_profile)
        foodButtonList.add(button_food9_profile)
        foodButtonList.add(button_food10_profile)
        //TODO: ButtonOther
    }
}
