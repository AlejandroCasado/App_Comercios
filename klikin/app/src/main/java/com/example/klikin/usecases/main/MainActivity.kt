package com.example.klikin.usecases.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.klikin.R
import com.example.klikin.databinding.ActivityMainBinding
import com.example.klikin.modelo.domain.ShopApi
import com.example.klikin.modelo.domain.CategoryShops
import com.example.klikin.provider.ProviderCategory
import com.example.klikin.usecases.detailShop.ShopRouter
import com.example.klikin.usecases.adapter.CategoryAdapter
import com.example.klikin.usecases.adapter.ShopsAdapter
import com.example.klikin.utils.Utils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity(), ShopsAdapter.OnClickShop, CategoryAdapter.OnClickCategory {

    private lateinit var binding:ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var connectivityManager: ConnectivityManager

    private var adapter: ShopsAdapter? = null
    private var adapterCategory: CategoryAdapter? = null
    private var rvShops:RecyclerView? = null
    private var rvCategory:RecyclerView? = null
    private var myLocation: Location? = null
    private val REQUEST_CODE_PERMISSION = 200
    private var distanceFiltered = 1000f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val screenSplash = installSplashScreen()
        screenSplash.setKeepOnScreenCondition{false}
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        connectivityManager = getSystemService(ConnectivityManager::class.java)

        setUpActionBar()
        init()
    }

    private fun init(){
        initFindViews()
        initPermission()

        controlNetworkAndGetListShop()
        observers()
    }

    private fun observers() {
        observeGetListAllShops()
        observeGetSizeListShops()
        observeGetListAllShopsWithDistance()
        observeGetNumShopsByDistance()
    }

    private fun observeGetNumShopsByDistance() {
        viewModel.getNumShopsByDistance.observe(this) { numShops ->
            numShops?.let {
                binding.txtNumShopsDistance.text = it.toString()
            }
            Log.i(localClassName,"response num shop by distance")
        }
    }

    private fun observeGetListAllShopsWithDistance() {
        viewModel.listAllShopsWithDistance.observe(this) { list ->
            list?.let {
                adapter?.setList(list)
                rvShops?.scrollToPosition(0)
                controlCardFiltered(list,distanceFiltered)
            }
            Log.i(localClassName,"response list all shop with distance")

        }
    }

    private fun observeGetSizeListShops() {
        viewModel.getSizeListShops.observe(this) { numShops ->
            numShops?.let {
                binding.txtNumShops.text = it.toString()
            }
            Log.i(localClassName,"response size list shops")
        }
    }

    private fun observeGetListAllShops() {
        viewModel.listAllShops.observe(this) { list ->
            list?.let {
                setUpRv(it)
                controlCardAllShops(it.size, true)
                rvShops?.scrollToPosition(0)
            }
            Log.i(localClassName,"response list all shop")
        }

    }

    private fun controlNetworkAndGetListShop() {
        if (Utils().checkForInternet(this)) {
            viewModel.getListShops(this)
            Log.i(localClassName,"Connection network")

        } else {
            Toast.makeText(this, getString(R.string.txt_no_Connection), Toast.LENGTH_SHORT).show()
            Log.i(localClassName,"No connection network")

        }
    }

    private fun initPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getMyLocation()
            Log.i(localClassName,"permission granted")

        }else{
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE_PERMISSION)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                myLocation = Location("MyLocation")
                myLocation!!.longitude = (location?.longitude ?: 0) as Double
                myLocation!!.latitude = (location?.latitude ?: 0) as Double
            }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE_PERMISSION){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getMyLocation()
            }else{
                showPopUpPermissionDenied()
            }
        }
    }

    private fun controlCardAllShops(size: Int, clicked:Boolean) {
        binding.cardAllShops.backgroundTintList = AppCompatResources.getColorStateList(this,if(clicked) R.color.blue_dark else R.color.white)
        binding.txtNumShops.setTextColor(if(clicked) getColor(R.color.white) else getColor(R.color.orange))
        viewModel.setNumList(size)
        binding.txtHelpShops.setTextColor(if(clicked) getColor(R.color.white) else getColor(R.color.gray))
    }

    private fun setUpRv(list: List<ShopApi>) {
        binding.progressbar.visibility = View.GONE
        rvShops?.visibility = View.VISIBLE
        adapter = ShopsAdapter(this,list,this)
        rvShops?.adapter = adapter
    }

    private fun initFindViews() {
        setUpRvShops()
        setUpRVCategories()

        logicClickCardAllShops()
        logicClickCardOrderByDistance()
    }

    private fun logicClickCardOrderByDistance() {
        binding.cardDistance.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                myLocation?.let {
                    logicClickDistanceFiltered(1000f,it,"1")

                }
            } else {
                showPopUpPermissionDenied()
            }
        }
        binding.cardDistance.setOnLongClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                myLocation?.let { location->
                    showDialogPopUpChooseDistance(location)
                }
            } else {
                showPopUpPermissionDenied()
            }

            true
        }
    }

    private fun showDialogPopUpChooseDistance(location: Location) {
        val popUp = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.layout_show_dialog_distance, null)
        val distanceOne = view.findViewById<TextView>(R.id.txt_one_distance)
        val distanceFive = view.findViewById<TextView>(R.id.txt_five_distance)
        val distanceTen = view.findViewById<TextView>(R.id.txt_ten_distance)
        popUp.setView(view)
        popUp.setCancelable(false)

        val dialog = popUp.create()
        dialog.show()

        logicClickOptionPopup(distanceOne, dialog, location, distanceFive, distanceTen)
    }

    private fun logicClickOptionPopup(
        distanceOne: TextView,
        dialog: AlertDialog,
        location: Location,
        distanceFive: TextView,
        distanceTen: TextView
    ) {
        distanceOne.setOnClickListener {
            dialog.dismiss()
            logicClickDistanceFiltered(1000f, location, "1")
        }

        distanceFive.setOnClickListener {
            dialog.dismiss()
            logicClickDistanceFiltered(5000f, location, "5")
        }

        distanceTen.setOnClickListener {
            dialog.dismiss()
            logicClickDistanceFiltered(10000f, location, "10")
        }
    }

    private fun logicClickDistanceFiltered(distance: Float, location: Location,num: String){
        val list = adapter?.getList()
        distanceFiltered = distance
        adapter?.setVisibilityDistanceShops(true)
        viewModel.calculateDistance(location, list)
        binding.txtHelpDistance.text = getString(R.string.txt_ordered_by_distance,num)
    }

    private fun logicClickCardAllShops() {
        binding.cardAllShops.setOnClickListener {
            val originalList = adapter?.getList()
            controlCardAllShops(originalList?.size ?: 0, true)
            adapter?.setList(originalList)
            adapter?.setVisibilityDistanceShops(false)
        }
    }

    private fun controlCardFiltered(list: List<ShopApi>, distance: Float) {
        binding.txtNumShopsDistance.visibility = View.VISIBLE
        viewModel.getNumShopByDistance(distance,list)
    }

    private fun showPopUpPermissionDenied() {
        val popUp = AlertDialog.Builder(this)
        popUp.setMessage(getString(R.string.txt_permission_location))
        popUp.setCancelable(false)
        popUp.setPositiveButton(getString(R.string.txt_btn_accept)) { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        popUp.create()
        popUp.show()
    }

    private fun setUpRVCategories() {
        rvCategory = binding.rvCategory
        val layoutManagerCategory: RecyclerView.LayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        rvCategory?.layoutManager = layoutManagerCategory
        val list = ProviderCategory().getCategories()
        adapterCategory = CategoryAdapter(this, list, this)
        rvCategory?.adapter = adapterCategory
    }

    private fun setUpRvShops() {
        rvShops = binding.rvShops
        val layoutManagerMyShops: RecyclerView.LayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        rvShops?.layoutManager = layoutManagerMyShops
    }

    private fun initializeCustomActionBar(): View {
        val view: View = layoutInflater.inflate(R.layout.view_action_bar, null)
        val titleActionBar = view.findViewById<TextView>(R.id.title)
        titleActionBar.text = getString(R.string.txt_name_main_activity)
        val chatArrowActionBar = view.findViewById<ImageButton>(R.id.ib_backpressed)
        chatArrowActionBar.visibility = View.GONE
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

    override fun onClickItem(shop: ShopApi) {
        ShopRouter(shop,myLocation).launch(this)
    }

    override fun onClickItemCategory(category: CategoryShops) {
        val list = adapter?.getList()
        val filteredList= viewModel.getFilteredListWithCategory(list,category)
        controlCardAllShops(filteredList?.size?:0,false)
        adapter?.setVisibilityDistanceShops(false)
        adapter?.setList(filteredList)
        rvShops?.scrollToPosition(0)

    }

}