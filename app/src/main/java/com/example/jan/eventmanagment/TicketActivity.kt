package com.example.jan.eventmanagment

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.jan.eventmanagment.Extensions.loadCurrentStudentId
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_ticket.image_ticket_qrcode

class TicketActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket)

    }

    private fun showQRCode() {
        val height = image_ticket_qrcode.height
        val width =  image_ticket_qrcode.width
        val eventId = 1
        val studentId = loadCurrentStudentId(this)
        val number =  String.format("%05d", eventId);


        val code = "$eventId$studentId"
        val multiFormatWriter = MultiFormatWriter()
        val bitmMatrix = multiFormatWriter.encode(code, BarcodeFormat.QR_CODE, 500, 500)
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.createBitmap(bitmMatrix)
        image_ticket_qrcode.setImageBitmap(bitmap)

    }
}
