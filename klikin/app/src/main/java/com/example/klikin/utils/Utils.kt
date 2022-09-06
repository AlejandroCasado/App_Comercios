package com.example.klikin.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import com.example.klikin.R

class Utils {

    companion object {

        fun setUpStatusAndNavigationBar(window: Window,context: Context,colorStatusBar: Int,colorNavigationBar: Int) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = context.getColor(colorStatusBar)
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                window.insetsController?.let {
                    it.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS)
                }
                changeColorNavigationBar(window,context,colorNavigationBar)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    changeColorNavigationBar(window,context,colorNavigationBar)
                }
            }
        }

        private fun changeColorNavigationBar(window: Window, context: Context, color:Int) {
            val styleId = getStyleNavigation(context)
            if(styleId == 2){ //Gesture
                window.navigationBarColor = context.getColor(color)
            }else{
                window.navigationBarDividerColor = context.getColor(R.color.white)
                window.navigationBarColor = context.getColor(R.color.black)
            }
        }

        private fun getStyleNavigation(context: Context): Int {
            val resources: Resources = context.resources
            val resourceId: Int =
                resources.getIdentifier("config_navBarInteractionMode", "integer", "android")
            return if (resourceId > 0) {
                resources.getInteger(resourceId)
            } else 0
        }
    }

    @SuppressLint("ServiceCast")
    fun checkForInternet(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

}