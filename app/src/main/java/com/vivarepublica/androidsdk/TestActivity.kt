package com.vivarepublica.androidsdk

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.vivarepublica.androidsdk.databinding.ActivityTestBinding
import com.vivarepublica.loginsdk.TossLoginController
import com.vivarepublica.loginsdk.TossSdk

class TestActivity : ComponentActivity() {

    private lateinit var binding: ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appKey = getString(R.string.toss_alpha_api_key)
        TossSdk.init(appKey, true)

        binding.btnTossLogin.setOnClickListener {
            TossLoginController.login(context = this) { tossLoginResult ->
                binding.txtTossLogin.text = tossLoginResult.toString()
            }
        }

        binding.btnGoToPlaystore.setOnClickListener {
            TossLoginController.moveToPlaystore(context = this)
        }
    }

}
