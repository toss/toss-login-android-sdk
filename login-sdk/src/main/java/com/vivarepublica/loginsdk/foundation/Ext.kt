package com.vivarepublica.loginsdk.foundation

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts

fun packageName(context: Context): String {
    return context.packageName
}

fun appVersion(context: Context): String {
    val packageManager = context.packageManager
    return runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(packageName(context), PackageManager.PackageInfoFlags.of(0)).versionName
        } else {
            @Suppress("DEPRECATION") packageManager.getPackageInfo(packageName(context), 0).versionName
        }
    }.getOrDefault("")
}


fun isTossInstalled(context: Context): Boolean {
    val packageManager = context.packageManager
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(Constants.TOSS_PACKAGE_NAME, PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION") packageManager.getPackageInfo(Constants.TOSS_PACKAGE_NAME, 0)
        }
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}


fun ActivityResultCaller.registerForActivityResult(callback: (ActivityResult) -> Unit) =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        callback(activityResult)
    }
