package com.vivarepublica.loginsdk.foundation

open class TossSdkError(
    open val code: String,
    override val message: String,
) : Exception(message)

data class NotExistAppKeyError(
    override val message: String = "AppKey is not exist.",
) : TossSdkError("-1", message)

data class LoginError(
    override val code: String,
    override val message: String,
    val url: String?
) : TossSdkError(code, message)