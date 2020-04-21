package com.htec.task.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object ConnectivityUtil {

    fun hasActiveInternetConnection(context: Context): Boolean {
        var result: Boolean = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworks = connectivityManager.allNetworks

        if (activeNetworks.isNotEmpty()) {
            for (i in activeNetworks.indices) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetworks[i])
                if (networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    result = true
                    break
                }
            }
        }
        return result
    }
}