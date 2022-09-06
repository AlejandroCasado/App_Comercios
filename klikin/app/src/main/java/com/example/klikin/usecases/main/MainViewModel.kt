package com.example.klikin.usecases.main

import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.klikin.modelo.domain.ShopApi
import com.example.klikin.modelo.domain.CategoryShops
import com.example.klikin.provider.apiProvider.ApiService
import com.example.klikin.provider.apiProvider.ApiServiceImp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.streams.toList

class MainViewModel: ViewModel() {
    private val _listAllShops = MutableLiveData<List<ShopApi>?>()
    val listAllShops get() = _listAllShops

    private val _getSizeListShops = MutableLiveData<Int>()
    val getSizeListShops get() = _getSizeListShops

    private val _listAllShopsWithDistance = MutableLiveData<List<ShopApi>?>()
    val listAllShopsWithDistance get() = _listAllShopsWithDistance

    private val _getNumShopsByDistance = MutableLiveData<Int?>()
    val getNumShopsByDistance get() = _getNumShopsByDistance

    fun getListShops(context: Context){
        val api: ApiService = ApiServiceImp().getApi()
        viewModelScope.launch {
            _listAllShops.postValue(withContext(Dispatchers.IO) {
                val response = api.getInfoEmpress()
                if (response.isSuccessful) {
                    response.body()
                } else
                    arrayListOf()
            })
        }
    }

    fun getFilteredListWithCategory(list: List<ShopApi>?, category: CategoryShops): List<ShopApi>? =
        list?.stream()?.parallel()?.filter {
            category.name == it.category

        }?.toList()

    fun setNumList(size: Int) {
        _getSizeListShops.value = size
    }

    fun calculateDistance(myLocation: Location, list: List<ShopApi>?) {
        var newlist = arrayListOf<ShopApi>()
        viewModelScope.launch {
           _listAllShopsWithDistance.postValue(withContext(Dispatchers.IO){
                list?.let { it ->
                    it.stream().parallel().forEach { shop->
                        val locationShop = Location("LocationShop")
                        locationShop.latitude = shop.latitude.toDouble()
                        locationShop.longitude = shop.longitude.toDouble()
                        shop.distancia = myLocation.distanceTo(locationShop)
                    }
                    val comparator = compareBy(ShopApi::distancia)
                    newlist.addAll(list)
                    newlist.sortedWith(comparator)
                }
            })
        }
    }

    fun getNumShopByDistance(maxDistance: Float, list: List<ShopApi>?) {
        var numShops = 0
        viewModelScope.launch {
            _getNumShopsByDistance.postValue(withContext(Dispatchers.IO) {
                list?.let {
                    it.stream().parallel().forEach {
                        if (it.distancia ?: 0f <= maxDistance)
                            numShops++
                    }
                }
                numShops
            })
        }
    }
}