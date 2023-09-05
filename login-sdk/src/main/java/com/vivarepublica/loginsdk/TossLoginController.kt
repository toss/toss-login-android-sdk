package com.vivarepublica.loginsdk

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.vivarepublica.loginsdk.foundation.*
import com.vivarepublica.loginsdk.foundation.Constants.LOGIN_TIME_OUT
import com.vivarepublica.loginsdk.model.TossLoginResult
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

object TossLoginController {

    internal val loginResultHandler = MutableSharedFlow<TossLoginResult>()

    fun isLoginAvailable(context: Context): Boolean =
        isTossInstalled(context)

    fun moveToPlaystore(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(TossUrl.playStoreUrl))
        context.startActivity(intent)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun login(context: Context, timeoutMs: Long = LOGIN_TIME_OUT, onResult: (TossLoginResult) -> Unit) {
        GlobalScope.launch {
            val loginUrl = createLoginUrl(context)

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(loginUrl))
            context.startActivity(intent)

            try {
                withTimeout(timeoutMs) {
                    val result = loginResultHandler.first()
                    withContext(Dispatchers.Main) {
                        onResult(result)
                    }
                }
            } catch (e: Throwable) {
                withContext(Dispatchers.Main) {
                    onResult(TossLoginResult.Error(TossSdkError("error", "${e.message}")))
                }
            }
        }
    }

    private fun createLoginUrl(context: Context): String {

        fun createRedirectUrlPrefix(): String {
            val appKey = TossSdk.appKey ?: throw NotExistAppKeyError()
            val redirectScheme = "${TossUrl.redirectScheme}${appKey}"
            return "$redirectScheme://oauth"
        }

        val loginUrl = Uri.Builder()
            .scheme(TossUrl.serviceScheme)
            .authority("oauth2")
            .appendQueryParameter("clientId", TossSdk.appKey ?: throw NotExistAppKeyError())
            .appendQueryParameter("redirect_uri", createRedirectUrlPrefix())
            .appendQueryParameter("sdk_version", Constants.SDK_VERSION)
            .appendQueryParameter("device", "android-${Constants.OS_VERSION}")
            .appendQueryParameter("version", appVersion(context))
            .appendQueryParameter("origin", packageName(context))
            .build()

        return loginUrl.toString()
    }


}