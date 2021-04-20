package dev.ran.MangoStore.Api
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
class NetworkCheck {
    fun networkisConnected(activity: Activity): Boolean {
        val conManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val internetInfo = conManager.activeNetworkInfo
        return internetInfo !=null
    }
}