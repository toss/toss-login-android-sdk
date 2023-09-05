package com.vivarepublica.loginsdk.foundation

import android.util.Log
import com.vivarepublica.loginsdk.TossSdk

private const val TAG = "TossSdkLog"

internal fun sdkDebugLog(message: String) {
    if (TossSdk.loggingEnabled) {
        Log.d(TAG, message)
    }
}

internal fun sdkErrorLog(error: TossSdkError) {
    if (TossSdk.loggingEnabled) {
        Log.e(TAG, "errorCode : ${error.code}, errorMessage : ${error.message}")
    }
}
