

package com.sphtech.shared.util

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import javax.inject.Inject
import android.net.NetworkCapabilities



/**
 * Checks if a network connection exists.
 */
open class NetworkUtils @Inject constructor(val context: Context) {

    open fun hasNetworkConnection(): Boolean {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)  // need ACCESS_NETWORK_STATE permission
        val isOnline =
            capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting && isOnline
    }
}
