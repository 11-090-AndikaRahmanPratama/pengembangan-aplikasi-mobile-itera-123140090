package com.newsfeed.myprofileapp

import android.content.Context
import android.net.ConnectivityManager
import android.os.BatteryManager
import android.os.Build
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

// 1. Device Info
interface DeviceInfo { fun getInfo(): String }
class DeviceInfoImpl : DeviceInfo {
    override fun getInfo(): String = "${Build.MANUFACTURER} ${Build.MODEL} (Android ${Build.VERSION.RELEASE})"
}

// 2. Battery Info
interface BatteryInfo { fun getLevel(): Int }
class BatteryInfoImpl(private val context: Context) : BatteryInfo {
    override fun getLevel(): Int {
        val bm = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }
}

// 3. Network Monitor (Real-time Version)
interface NetworkMonitor {
    val isConnected: Flow<Boolean>
}

class NetworkMonitorImpl(context: Context) : NetworkMonitor {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override val isConnected: Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: android.net.Network) { trySend(true) }
            override fun onLost(network: android.net.Network) { trySend(false) }
        }
        connectivityManager.registerDefaultNetworkCallback(callback)
        trySend(connectivityManager.activeNetwork != null)
        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged()
}