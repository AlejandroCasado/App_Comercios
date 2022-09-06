package com.example.klikin.usecases.detailShop

import android.content.Context
import android.content.Intent
import android.location.Location
import com.example.klikin.modelo.domain.ShopApi
import com.example.klikin.usecases.baseRouter.BaseRouter
import java.lang.Exception

class ShopRouter(private val shop: ShopApi, private val myLocation: Location?):
    BaseRouter.BaseActivityRouter {

    companion object{
        const val INTENT_NAME_SHOP = "name"
        const val INTENT_LONGITUDE_SHOP = "longitude"
        const val INTENT_LATITUDE_SHOP = "latitude"
        const val INTENT_INSTAGRAM_SHOP = "instagram"
        const val INTENT_TWITTER_SHOP = "twitter"
        const val INTENT_FACEBOOK_SHOP = "facebook"
        const val INTENT_DESCRIPTION_SHOP = "description"
        const val INTENT_IMAGE_SHOP = "image"
        const val INTENT_MY_LATITUDE_SHOP = "mylatitude"
        const val INTENT_MY_LONGITUDE_SHOP = "mylongitude"
        const val INTENT_ICON_SHOP = "icon"
        const val INTENT_CONTACT_SHOP = "contact"
        const val INTENT_EMAIL_SHOP = "email"


    }
    override fun intent(activity: Context): Intent = Intent(activity, ShopActivity::class.java).apply {
        putExtra(INTENT_NAME_SHOP, shop.name)
        putExtra(INTENT_LATITUDE_SHOP, shop.latitude)
        putExtra(INTENT_LONGITUDE_SHOP, shop.longitude)
        putExtra(INTENT_INSTAGRAM_SHOP, shop.social.instagram)
        putExtra(INTENT_TWITTER_SHOP, shop.social.twitter)
        putExtra(INTENT_FACEBOOK_SHOP, shop.social.facebook)
        putExtra(INTENT_DESCRIPTION_SHOP, shop.description)
        putExtra(INTENT_CONTACT_SHOP, shop.contact.phone)
        putExtra(INTENT_EMAIL_SHOP, shop.contact.email)

        myLocation?.let {
            putExtra(INTENT_MY_LONGITUDE_SHOP, it.longitude.toString())
            putExtra(INTENT_MY_LATITUDE_SHOP, it.latitude.toString())
        }

        var url: String? = null
        try {
            url = shop.ipadPhotos.welcome.thumbnails.large
        }catch (e:Exception){ }
        if(url == null) url = ""
        putExtra(INTENT_IMAGE_SHOP, url)

        var icon = ""
        shop.photos.let {
            if (it.isNotEmpty()) {
                icon = it[0].url
            }
        }
        putExtra(INTENT_ICON_SHOP, icon)

    }
}