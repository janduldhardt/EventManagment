package com.example.jan.eventmanagment.Extensions

import android.app.Application


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {

        var context: MyApplication? = null
            private set
    }
}