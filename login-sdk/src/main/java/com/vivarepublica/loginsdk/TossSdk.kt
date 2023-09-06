package com.vivarepublica.loginsdk

object TossSdk {

    internal var loggingEnabled: Boolean = false
    internal var appKey: String? = null

    fun init(
        appKey: String,
        loggingEnabled: Boolean? = null,
    ) {
        TossSdk.appKey = appKey
        TossSdk.loggingEnabled = loggingEnabled ?: false
    }

}
