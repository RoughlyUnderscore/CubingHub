package com.roughlyunderscore.cubinghub.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object NetworkUtils {
  fun isNetworkAvailable(context: Context): Boolean {
    val connManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      val capabilities = connManager.getNetworkCapabilities(connManager.activeNetwork)
      when {
        capabilities == null -> false

        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
          capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
          capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true

        else -> false
      }
    } else {
      connManager.activeNetworkInfo != null && connManager.activeNetworkInfo?.isConnected == true
    }
  }
}