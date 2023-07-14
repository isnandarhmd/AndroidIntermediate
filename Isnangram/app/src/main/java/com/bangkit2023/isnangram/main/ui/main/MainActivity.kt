package com.bangkit2023.isnangram.main.ui.main

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bangkit2023.isnangram.R
import com.bangkit2023.isnangram.databinding.ActivityMainBinding
import com.bangkit2023.isnangram.main.ui.discover.HomeFragment
import com.bangkit2023.isnangram.main.ui.upload.CameraActivity
import com.bangkit2023.isnangram.utils.Helper

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var startNewStory: ActivityResultLauncher<Intent>
    private lateinit var fragmentHome: HomeFragment

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragmentHome = HomeFragment()

        startNewStory =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    fragmentHome.onRefresh()
                }
            }

        switchFragment(fragmentHome)

        binding.fab.setOnClickListener {
            if (Helper.isPermissionGranted(this, Manifest.permission.CAMERA)) {
                val intent = Intent(this@MainActivity, CameraActivity::class.java)
                startNewStory.launch(intent)
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_CODE
                )
            }
        }
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    companion object {
        const val CAMERA_PERMISSION_CODE = 10
    }
}