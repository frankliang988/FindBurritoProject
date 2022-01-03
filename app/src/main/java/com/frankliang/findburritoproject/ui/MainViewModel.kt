package com.frankliang.findburritoproject.ui

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.exception.ApolloException
import com.frankliang.findburritoproject.model.Coordinate
import com.frankliang.findburritoproject.model.Restaurant
import com.frankliang.findburritoproject.network.NetworkRepository
import com.frankliang.findburritoproject.util.CoordinateUtil
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val INITIAL_PAGE = 0
    }
    val restaurantList = MutableLiveData<List<Restaurant>>()
    val isShowProgressBar = MutableLiveData<Boolean>()
    val isNetworkError = MutableLiveData<Boolean>()
    var coordinate: Coordinate? = null
    private var isLoadingData = false

    init {
        isShowProgressBar.value = false
        isNetworkError.value = false
    }

    fun loadInitialData(newCoordinates: Coordinate) {
        synchronized (this) {
            if(!shouldReloadData(newCoordinates))
                return
            isLoadingData = true
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                setShowProgressBar(true)
                this@MainViewModel.coordinate = newCoordinates
                setRestaurantListValue(mutableListOf())
                queryDataFromYelp(INITIAL_PAGE)
            }
        }
    }

    fun loadMoreData(page: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                setShowProgressBar(true)
                queryDataFromYelp(page)
            }
        }
    }

    /**
     * reload data if:
     * 1) no cached coordinates
     * 2) no data is loaded
     * 3) user has moved too far
     */
    private fun shouldReloadData(newCoordinates: Coordinate): Boolean {
        if(isLoadingData) return false
        return this@MainViewModel.coordinate == null
                || restaurantList.value.isNullOrEmpty()
                || CoordinateUtil.hasUserMovedPassThreshold(this@MainViewModel.coordinate!!, newCoordinates)

    }

    private suspend fun queryDataFromYelp(page: Int) {
        if(this.coordinate == null) {
            isLoadingData = false
            return
        }
        try {
            val result = NetworkRepository.findRestaurants(coordinate!!, page)
            result.data?.let {data ->
                data.search?.let { search ->
                    val list= restaurantList.value?.toMutableList() ?: mutableListOf()
                    search.business?.forEach{business ->
                        business?.let { list.add(Restaurant(it)) }
                    }
                    setRestaurantListValue(list)
                }
            }
            setIsNetworkError(false)
        } catch (e: ApolloException) {
            Log.d("MainViewModel", "${e.printStackTrace()}")
            setIsNetworkError(true)
        }
        setShowProgressBar(false)
        isLoadingData = false
    }

    private suspend fun setShowProgressBar(isLoading: Boolean) {
        withContext(Dispatchers.Main) {
            isShowProgressBar.value = isLoading
        }
    }

    private suspend fun setIsNetworkError(isError: Boolean) {
        withContext(Dispatchers.Main) {
            isNetworkError.value = isError
        }
    }

    private suspend fun setRestaurantListValue(list: MutableList<Restaurant>) {
        withContext(Dispatchers.Main) {
            if(list.isNullOrEmpty() && restaurantList.value.isNullOrEmpty()) return@withContext
            restaurantList.value = list
        }
    }
}