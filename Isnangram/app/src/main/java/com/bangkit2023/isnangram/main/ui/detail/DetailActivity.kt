package com.bangkit2023.isnangram.main.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bangkit2023.isnangram.R
import com.bangkit2023.isnangram.data.local.entity.StoryEntity
import com.bangkit2023.isnangram.databinding.ActivityDetailBinding
import com.bangkit2023.isnangram.utils.Const.EXTRA_DETAIL
import com.bangkit2023.isnangram.utils.Helper.setLocalDateFormat

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val story = intent.getParcelableExtra<StoryEntity>(EXTRA_DETAIL)
        showDetailStory(story)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showDetailStory(story: StoryEntity?) {
        if (story != null) {
            binding.apply {
                tvDetailName.text = story.name
                tvDetailDescription.text = story.description
                val loading = CircularProgressDrawable(this@DetailActivity)
                loading.setColorSchemeColors(
                    ContextCompat.getColor(this@DetailActivity, R.color.teal_200),
                    ContextCompat.getColor(this@DetailActivity, R.color.teal_700),
                    ContextCompat.getColor(this@DetailActivity, R.color.gray)
                )
                loading.centerRadius = 30f
                loading.strokeWidth = 5f
                loading.start()
                tvCreated.setLocalDateFormat(story.createdAt)
                Glide.with(this@DetailActivity)
                    .load(story.photoUrl)
                    .placeholder(loading)
                    .into(ivDetailPhoto)

            }
        }
    }
}
