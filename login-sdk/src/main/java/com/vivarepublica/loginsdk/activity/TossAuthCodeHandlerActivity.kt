package com.vivarepublica.loginsdk.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.vivarepublica.loginsdk.TossLoginController
import com.vivarepublica.loginsdk.model.toTossLoginResult
import kotlinx.coroutines.launch

class TossAuthCodeHandlerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val url = intent.data
            url?.toTossLoginResult()?.let {
                TossLoginController.loginResultHandler.emit(it)
            }
            finish()
        }

    }

}
