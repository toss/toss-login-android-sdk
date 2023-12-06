package com.vivarepublica.loginsdk

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.vivarepublica.loginsdk.foundation.*
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
    fun login(
        context: Context,
        policy: String? = null,
        onResult: (TossLoginResult) -> Unit
    ) {
        GlobalScope.launch {
            val loginUrl = createLoginUrl(context, policy)

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(loginUrl))
            context.startActivity(intent)

            val result = loginResultHandler.first()
            withContext(Dispatchers.Main) {
                onResult(result)
            }
        }
    }

    private fun createLoginUrl(
        context: Context,
        policy: String? = null
    ): String {

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
            .apply {
                policy?.let {
                    appendQueryParameter("oauth_policy", policy)
                }
            }
            .build()

        return loginUrl.toString()
    }


}