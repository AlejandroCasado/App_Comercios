package com.example.klikin.usecases.baseRouter.otherRouter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Browser
import com.example.klikin.usecases.baseRouter.BaseRouter

class LinksRouter(private val uri:Uri, private val context: Context): BaseRouter.BaseActivityRouter {

    override fun intent(activity: Context): Intent = Intent(Intent.ACTION_VIEW,uri).apply {
        putExtra(Browser.EXTRA_APPLICATION_ID,context.packageName)
    }
}