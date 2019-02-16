package com.example.eventmanagement19.extensions

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, parent: ViewGroup? = this) {
    LayoutInflater.from(context).inflate(layoutRes, parent)
}

fun View.inflate(@LayoutRes layoutRes: Int, parent: ViewGroup? = null) {
    LayoutInflater.from(context).inflate(layoutRes, parent)
}