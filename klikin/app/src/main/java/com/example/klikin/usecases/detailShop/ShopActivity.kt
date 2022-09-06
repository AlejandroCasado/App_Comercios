package com.example.klikin.usecases.detailShop

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.klikin.R
import com.example.klikin.databinding.ActivityShopBinding
import com.example.klikin.modelo.api.Social
import com.example.klikin.modelo.domain.ShopInfo
import com.example.klikin.usecases.baseRouter.otherRouter.GoogleMapsRouter
import com.example.klikin.usecases.baseRouter.otherRouter.LinksRouter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception


class ShopActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityShopBinding
    private lateinit var viewModel: ShopViewModel
    private lateinit var map:GoogleMap
    private lateinit var shopInfo:ShopInfo

    private var titleActionBar: TextView? = null
    private var myLocation:Location? = null
    private var numRedsSocials: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[ShopViewModel::class.java]
        setContentView(binding.root)
        setUpActionBar()
        init()
    }

    private fun init(){
        initBundle()
        controlGoogleMap()
        controlDrawIconShop()
        controlDrawSocialMedia()
        controlDrawDescriptionShop()
        controlDrawEmailAndPhone()
    }

    private fun controlDrawEmailAndPhone() {
        if (shopInfo.email.isEmpty()) {
            binding.llEmail.visibility = View.GONE
        }
        binding.txtEmail.text = shopInfo.email

        if (shopInfo.phone.isEmpty()) {
            binding.llPhone.visibility = View.GONE
        }
        binding.txtPhone.text = shopInfo.phone
    }

    private fun controlDrawDescriptionShop() {
        if (shopInfo.description.isEmpty()) {
            binding.cardDescription.visibility = View.GONE
        }
        binding.txtDescription.text = shopInfo.description
    }

    private fun controlDrawSocialMedia() {
        drawSocialMedia()
        logicClickSocialMedia()
    }

    private fun logicClickSocialMedia() {
        binding.llInstagram.setOnClickListener {
            goToLinkWithUrl(shopInfo.social.instagram)
        }
        binding.llTwitter.setOnClickListener {
            goToLinkWithUrl(shopInfo.social.twitter)
        }
        binding.llFacebook.setOnClickListener {
            goToLinkWithUrl(shopInfo.social.facebook)
        }
    }

    private fun drawSocialMedia() {
        if (shopInfo.social.instagram.isEmpty()) {
            binding.llInstagram.visibility = View.GONE
            numRedsSocials++
        }
        if (shopInfo.social.twitter.isEmpty()) {
            binding.llTwitter.visibility = View.GONE
            numRedsSocials++
        }
        if (shopInfo.social.facebook.isEmpty()) {
            binding.llFacebook.visibility = View.GONE
            numRedsSocials++
        }
        if (numRedsSocials == 3) {
            binding.cardRedSocial.visibility = View.GONE
        }
    }

    private fun controlDrawIconShop() {
        if (shopInfo.icon.isNotEmpty()) {
            Glide.with(this).load(shopInfo.icon).placeholder(R.drawable.placeholder)
                .into(binding.imageIconShop)

        } else {
            Glide.with(this).load(R.drawable.placeholder).into(binding.imageIconShop)
        }
    }

    private fun controlGoogleMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.goToMap.setOnClickListener {
            GoogleMapsRouter(Uri.parse("http://maps.google.com/maps?saddr=" + shopInfo.latitude + "," + shopInfo.longitude + "&daddr=" + myLocation?.latitude + "," + myLocation?.longitude)).launch(
                this
            )
        }
    }

    private fun initBundle(){
        val bundle: Bundle? = intent.extras
        bundle?.let {
            setUpShopInfo(it)
            setUpNameActivityAndImageShop(shopInfo.image,shopInfo.name)
        }
    }

    private fun setUpNameActivityAndImageShop(image: String, name: String) {
        titleActionBar?.text = name

        if (image.isEmpty()) {
            Glide.with(this).load(R.drawable.only_image).placeholder(R.drawable.placeholder)
                .into(binding.imageShop)

        } else {
            Glide.with(this).load(image).placeholder(R.drawable.placeholder).into(binding.imageShop)
        }
    }

    private fun setUpShopInfo(it: Bundle) {
        val name = it.getString(ShopRouter.INTENT_NAME_SHOP, "")
        val url = it.getString(ShopRouter.INTENT_IMAGE_SHOP, "")
        val description = it.getString(ShopRouter.INTENT_DESCRIPTION_SHOP, "")
        val longitude = it.getString(ShopRouter.INTENT_LONGITUDE_SHOP, "")
        val latitude = it.getString(ShopRouter.INTENT_LATITUDE_SHOP, "")
        val instagram = it.getString(ShopRouter.INTENT_INSTAGRAM_SHOP, "")
        val twitter = it.getString(ShopRouter.INTENT_TWITTER_SHOP, "")
        val facebook = it.getString(ShopRouter.INTENT_FACEBOOK_SHOP, "")
        val myLongitude = it.getString(ShopRouter.INTENT_MY_LONGITUDE_SHOP, "")
        val myLatitude = it.getString(ShopRouter.INTENT_MY_LATITUDE_SHOP, "")
        val iconShop = it.getString(ShopRouter.INTENT_ICON_SHOP, "")
        val contactPhoneShop = it.getString(ShopRouter.INTENT_CONTACT_SHOP, "")
        val contactEmailShop = it.getString(ShopRouter.INTENT_EMAIL_SHOP, "")
        val social = Social(twitter, facebook, instagram)
        shopInfo = ShopInfo(name, description, url, longitude, latitude, social, iconShop,contactPhoneShop,contactEmailShop)
        myLocation = Location("MyLocation")
        myLocation!!.longitude = myLongitude.toDouble()
        myLocation!!.latitude = myLatitude.toDouble()
    }

    private fun initializeCustomActionBar(): View {
        val view: View = layoutInflater.inflate(R.layout.view_action_bar, null)
        titleActionBar = view.findViewById(R.id.title)
        val chatArrowActionBar = view.findViewById<ImageButton>(R.id.ib_backpressed)
        chatArrowActionBar.visibility = View.VISIBLE
        chatArrowActionBar.setOnClickListener { onBackPressed() }

        return view
    }

    private fun setUpActionBar() {
        val actionBar = this.supportActionBar
        actionBar?.let {
            val view = initializeCustomActionBar()
            it.customView = view
            it.setHomeButtonEnabled(false)
            it.setDisplayShowCustomEnabled(true)
            it.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
            it.show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker()
    }

    private fun createMarker() {
        val coordinate = LatLng(shopInfo.latitude.toDouble(),shopInfo.longitude.toDouble())
        val marker = MarkerOptions().position(coordinate).title(shopInfo.name)
        map.addMarker(marker)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate,16f),3000,null)
    }

    private fun goToLinkWithUrl(url:String){
        val uri = Uri.parse(url)
        try {
            LinksRouter(uri,baseContext).launch(this)
        }catch (e:Exception){ Log.e(localClassName,"Error click social media with url:$url")}
    }
}