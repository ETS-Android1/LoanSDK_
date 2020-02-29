package com.intelia.sdk.eligibility.ext

import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.provider.Settings
import org.json.JSONObject
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import androidx.core.content.ContextCompat.getSystemService
import android.telephony.TelephonyManager




fun String.hash(): String {
    val digest = MessageDigest.getInstance("SHA-1")
    val result = digest.digest(this.toByteArray(Charset.forName("UTF-8")))

// Another way to construct HEX, my previous post was only the method like your solution
    val sb = StringBuilder()

    for (b in result)
    // This is your byte[] result..
    {
        sb.append(String.format("%02X", b))
    }

    return sb.toString()
}

fun Context.ip(): String {
    val wm = this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager?
    return wm?.connectionInfo?.ipAddress?.let {
        val ip = String.format(
            "%d.%d.%d.%d",
            it and 0xff,
            it shr 8 and 0xff,
            it shr 16 and 0xff,
            it shr 24 and 0xff
        )
        ip
    } ?: ""
}

fun Context.buildDeviceInfo(extra: JSONObject) {
    extra.put("version", System.getProperty("os.version"))
    extra.put("device", System.getProperty(android.os.Build.DEVICE))
    extra.put("model", System.getProperty(android.os.Build.MODEL))
    extra.put("product", System.getProperty(android.os.Build.PRODUCT))
    extra.put("imel", Settings.System.getString(this.contentResolver, Settings.Secure.ANDROID_ID))
}

fun Context.hash(): String {
    try {
        val info = packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_SIGNATURES
        )
        info.signatures.forEach {
            val md = MessageDigest.getInstance("SHA")
            md.update(it.toByteArray())
            return android.util.Base64.encodeToString(md.digest(), android.util.Base64.DEFAULT)
        }
    } catch (e: PackageManager.NameNotFoundException) {

    } catch (e: NoSuchAlgorithmException) {
    }

    return ""
}