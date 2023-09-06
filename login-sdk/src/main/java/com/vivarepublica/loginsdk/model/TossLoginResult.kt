package com.vivarepublica.loginsdk.model

import android.net.Uri
import com.vivarepublica.loginsdk.foundation.LoginError
import com.vivarepublica.loginsdk.foundation.TossSdkError

sealed class TossLoginResult {

    data class Success(val authCode: String) : TossLoginResult()
    data class Error(val error: TossSdkError) : TossLoginResult()
    object Cancelled : TossLoginResult()

}

fun Uri?.toTossLoginResult(): TossLoginResult {

    if (this == null) {
        return TossLoginResult.Error(LoginError("uri is null", "uri is null", "null"))
    }

    val authCode = getQueryParameter("code")
    if (authCode.isNullOrEmpty().not()) {
        return TossLoginResult.Success(authCode!!)
    }

    val errorCode = getQueryParameter("error") ?: "unknown"
    val errorDescription = getQueryParameter("error_description") ?: "unknown"
    val errorUri = getQueryParameter("error_uri")

    return if (errorCode == "cancelled") {
        TossLoginResult.Cancelled
    } else {
        TossLoginResult.Error(LoginError(errorCode, errorDescription, errorUri))
    }
}
