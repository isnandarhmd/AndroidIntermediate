package com.bangkit2023.isnangram.main.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit2023.isnangram.main.ui.auth.AuthActivity
import com.bangkit2023.isnangram.main.ui.main.MainActivity
import com.bangkit2023.isnangram.main.viewmodel.ViewModelFactory
import com.bangkit2023.isnangram.utils.Const.EXTRA_TOKEN

@SuppressLint("CustomSplashScreen")
class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ViewModelFactory.getInstance(this)
        val viewModel: SplashViewModel by viewModels { factory }
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.getUserToken().observe(this) { token ->
                if (token.isNullOrEmpty()) {
                    val intent = Intent(this, AuthActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(EXTRA_TOKEN, token)
                    startActivity(intent)
                    finish()
                }
            }
        }, 15000)
    }
}