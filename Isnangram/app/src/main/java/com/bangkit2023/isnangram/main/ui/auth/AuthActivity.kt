package com.bangkit2023.isnangram.main.ui.auth

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.bangkit2023.isnangram.R
import com.bangkit2023.isnangram.databinding.ActivityAuthBinding
import com.bangkit2023.isnangram.main.ui.main.MainActivity
import java.util.*

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragmentSetup()

        val handler = Handler(Looper.getMainLooper())
        val random = Random()
        val timeDuration = (1000..6000).random().toLong()
        val task: Runnable = object : Runnable {
            override fun run() {
                val widthDp = resources.displayMetrics.run { widthPixels / density }
                val translationX = random.nextInt(widthDp.toInt()).toFloat()
                ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_X, translationX).apply {
                    duration = timeDuration
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode = ObjectAnimator.REVERSE
                }.start()
                val translationY = random.nextInt(widthDp.toInt()).toFloat()
                ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_Y, translationY).apply {
                    duration = timeDuration
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode = ObjectAnimator.REVERSE
                }.start()
                handler.postDelayed(this, 3000)
            }
        }
        task.run()
    }

    private fun fragmentSetup() {
        supportFragmentManager.beginTransaction()
            .add(R.id.auth_container, LoginFragment())
            .commit()
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    fun navigateToLogin() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.auth_container, LoginFragment())
            .commit()
    }

    fun navigateToMain() {
        // Implement the navigation to the main activity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
