package com.example.klikin.usecases.baseRouter

import android.content.Context
import android.content.Intent

class BaseRouter {

    interface BaseActivityRouter{
        fun intent(activity: Context): Intent
        fun launch(activity: Context) = activity.startActivity(intent(activity))
    }
}