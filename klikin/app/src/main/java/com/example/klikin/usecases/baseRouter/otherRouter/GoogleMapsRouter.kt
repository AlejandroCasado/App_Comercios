package com.example.klikin.usecases.baseRouter.otherRouter

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.klikin.usecases.baseRouter.BaseRouter

class GoogleMapsRouter(private val uri: Uri) : BaseRouter.BaseActivityRouter {

    override fun intent(activity: Context): Intent = Intent(Intent.ACTION_VIEW, uri).apply {
       setClassName("com.google.android.apps.maps",
           "com.google.android.maps.MapsActivity")
    }
}