package com.intelia.sdk.eligibility.ext

import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import org.json.JSONObject
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


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

fun getDeviceID(): String? {
    return "35" + //we make this look like a valid IMEI
            Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10 //13 digits
}

fun buildDeviceInfo(extra: JSONObject) {
    extra.put("version", System.getProperty("os.version"))
    extra.put("device", System.getProperty(Build.DEVICE))
    extra.put("model", System.getProperty(Build.MODEL))
    extra.put("product", System.getProperty(Build.PRODUCT))
    extra.put(
        "imel",
        "imei" + getDeviceID()
    )
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