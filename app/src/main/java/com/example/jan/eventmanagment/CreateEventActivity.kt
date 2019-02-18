package com.example.jan.eventmanagment

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.jan.eventmanagment.Models.EventImageUpload
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_create_event.btn_chooseImage
import kotlinx.android.synthetic.main.activity_create_event.btn_eventPreview
import kotlinx.android.synthetic.main.activity_create_event.btn_upload_eventImage
import kotlinx.android.synthetic.main.activity_create_event.image_creatEvent_preview
import kotlinx.android.synthetic.main.activity_create_event.progressbar_upload_image
import kotlinx.android.synthetic.main.activity_create_event.text_input_eventCapacity
import kotlinx.android.synthetic.main.activity_create_event.text_input_eventDeadline
import kotlinx.android.synthetic.main.activity_create_event.text_input_eventDescription
import kotlinx.android.synthetic.main.activity_create_event.text_input_eventEndDate
import kotlinx.android.synthetic.main.activity_create_event.text_input_eventFacebook
import kotlinx.android.synthetic.main.activity_create_event.text_input_eventImageURL
import kotlinx.android.synthetic.main.activity_create_event.text_input_eventLine
import kotlinx.android.synthetic.main.activity_create_event.text_input_eventOrganizer
import kotlinx.android.synthetic.main.activity_create_event.text_input_eventStartDate
import kotlinx.android.synthetic.main.activity_create_event.text_input_eventTelephone
import kotlinx.android.synthetic.main.activity_create_event.text_input_eventTermsAndConditions
import kotlinx.android.synthetic.main.activity_create_event.text_input_eventTicketPrice
import kotlinx.android.synthetic.main.activity_create_event.text_input_eventTitle
import kotlinx.android.synthetic.main.activity_create_event.text_input_eventVenue
import kotlinx.android.synthetic.main.activity_create_event.text_input_xEventDeadline
import kotlinx.android.synthetic.main.activity_create_event.text_input_xEventEndDate
import kotlinx.android.synthetic.main.activity_create_event.text_input_xEventStartDate
import java.text.SimpleDateFormat
import java.util.Calendar

class CreateEventActivity : AppCompatActivity() {

    val PICK_IMAGE_REQUEST = 1 // Just a constant for our image loading can be any integer
    lateinit var mImageUri: Uri
    lateinit var mStorageRef: StorageReference
    lateinit var mDatabaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        mStorageRef = FirebaseStorage.getInstance().getReference("eventImages")
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("eventImages")

        text_input_eventStartDate.setOnClickListener {
            datePicker(text_input_eventStartDate)
        }
        text_input_eventEndDate.setOnClickListener {
            datePicker(text_input_eventEndDate)
        }

        text_input_eventDeadline.setOnClickListener {
            datePicker(text_input_eventDeadline)
        }

        btn_chooseImage.setOnClickListener {
            openFileChooser()
        }

        btn_upload_eventImage.setOnClickListener {
            uploadFile()
        }

        btn_eventPreview.setOnClickListener {
            if (validateAllinputs()) { //If there is no error we load the preview screen
                showPreview()
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }

        }


    }

    //This checks if any of inputs displays an error
    private fun validateAllinputs(): Boolean {
        return (validateMandatoryInput(text_input_eventTitle)
                && validateMandatoryInput(text_input_eventVenue)
                && validateMandatoryInput(text_input_xEventStartDate)
                && validateMandatoryInput(text_input_eventOrganizer)
                && validateMandatoryInput(text_input_eventDescription)
                && validateMandatoryInput(text_input_eventTelephone))

    }


    private fun reformatTime(inputString: String?): String? {
        val fromUser = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val myFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val reformattedStr = myFormat.format(fromUser.parse(inputString))
        return reformattedStr
    }

    fun validateMandatoryInput(input: TextInputLayout): Boolean {
        val stringInput = input.editText!!.text.toString().trim()
        if (stringInput.isEmpty()) {
            input.isErrorEnabled = true
            input.error = "Mandatory field"
            return false
        } else {
            input.error = null
            input.isErrorEnabled = false
        }
        if (input.isErrorEnabled) {
            return false
        }
        return true
    }


    private fun showPreview() {
        val pTitle = text_input_eventTitle.editText!!.text.toString()
        val pStartDate = text_input_xEventStartDate.editText!!.text.toString() //Time
        val pEndDate = text_input_xEventEndDate.editText!!.text.toString() //Time
        val pVenue = text_input_eventVenue.editText!!.text.toString()
        val pImageUrl = text_input_eventImageURL.text.toString()
        val pOrganizer = text_input_eventOrganizer.editText!!.text.toString()
        val pDescription = text_input_eventDescription.editText!!.text.toString()
        val pTermsAndConditions = text_input_eventTermsAndConditions.editText!!.text.toString()
        val pTelephoneNumber = text_input_eventTelephone.editText!!.text.toString()
        val pFacebook = text_input_eventFacebook.editText!!.text.toString()
        val pLine = text_input_eventLine.editText!!.text.toString()
        val pDeadline = text_input_xEventDeadline.editText!!.text.toString() //Time
        val pCapacity = text_input_eventCapacity.editText!!.text.toString() //Int
        val pTicketPrice = text_input_eventTicketPrice.editText!!.text.toString() //Int

        val intent = Intent(this@CreateEventActivity, EventPreviewActivity::class.java).apply {
            putExtra("pTitle", pTitle)
            putExtra("pStartDate", pStartDate)
            putExtra("pEndDate", pEndDate)
            putExtra("pVenue", pVenue)
            putExtra("pImageUrl", pImageUrl)
            putExtra("pOrganizer", pOrganizer)
            putExtra("pDescription", pDescription)
            putExtra("pTermsAndConditions", pTermsAndConditions)
            putExtra("pTelephoneNumber", pTelephoneNumber)
            putExtra("pFacebook", pFacebook)
            putExtra("pLine", pLine)
            putExtra("pDeadline", pDeadline)
            putExtra("pCapacity", pCapacity)
            putExtra("pTicketPrice", pTicketPrice)
        }
        startActivity(intent)

    }



    fun datePicker(editTextVar: android.support.design.widget.TextInputEditText) {
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)

        val dpd = DatePickerDialog(this, android.R.style.Theme_Holo_Dialog, DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->

            val tpd = TimePickerDialog(this, android.R.style.ThemeOverlay_Material_Dialog, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                val time = "$dayOfMonth/$monthOfYear/$year $hourOfDay:$minute"
                val formattedTime = reformatTime(time)
                editTextVar.setText(formattedTime)

            }, 0, 0, true)



            tpd.setOnShowListener {
                tpd.getButton(Dialog.BUTTON_NEGATIVE).visibility = View.GONE
            }
            tpd.show()
        }, year, month, day)

        dpd.setOnShowListener {
            dpd.getButton(Dialog.BUTTON_NEGATIVE).visibility = View.GONE
        }
        dpd.show()
    }

    //*****************************************************************************************************
    //*****************************************************************************************************
    //*************The functions are to pick and upload the Image********************************************

    //Only get the name of the file extension eg. JPG PNG etc
    private fun getFileExtension(uri: Uri): String {
        val cr = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(uri))
    }


    //uploads the file
    private fun uploadFile() {
        if (mImageUri != null) {

            val pathUpload = "" + System.currentTimeMillis() + "." + getFileExtension(mImageUri)

            val fileReference = mStorageRef.child(pathUpload)
            fileReference.putFile(mImageUri)
                    .addOnSuccessListener { it ->
                        Toast.makeText(this@CreateEventActivity, "Upload Successfull", Toast.LENGTH_SHORT).show()
                        val upload = EventImageUpload(it.storage.downloadUrl.toString())
                        val uploadId = mDatabaseRef.push().key
                        mDatabaseRef.child(uploadId!!).setValue(upload)

                        //******************************************************************
                        //*****This is only to get the URL from the object in FireBase*******
                        mStorageRef.child(pathUpload).downloadUrl.addOnSuccessListener {
                            text_input_eventImageURL.setText(it.toString())
                        }.addOnFailureListener {
                            text_input_eventImageURL.setText(it.message)
                        }
                        //*******************************************************************

                    }
                    .addOnFailureListener {
                        Toast.makeText(this@CreateEventActivity, it.message, Toast.LENGTH_LONG).show()
                    }
                    .addOnProgressListener {
                        var progress = (100 * it.bytesTransferred / it.totalByteCount)
                        progressbar_upload_image.progress = progress.toInt()
                    }
        }
    }


    //opens the file drawer
    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    //this loads the picked image in the placeholder
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.data != null) {
            mImageUri = data.data

            Glide.with(this).load(mImageUri).into(image_creatEvent_preview)
        }
    }
}
