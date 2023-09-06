package com.vivarepublica.loginsdk.foundation

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import com.vivarepublica.loginsdk.BuildConfig

object Constants {

    const val SDK_VERSION = BuildConfig.VERSION_NAME
    val OS_VERSION = Build.VERSION.SDK_INT
    const val TOSS_PACKAGE_NAME = "viva.republica.toss"
}