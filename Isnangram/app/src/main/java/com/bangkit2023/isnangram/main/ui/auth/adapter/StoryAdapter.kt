package com.bangkit2023.isnangram.main.ui.auth.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bangkit2023.isnangram.R
import com.bumptech.glide.Glide
import com.bangkit2023.isnangram.data.local.entity.StoryEntity
import com.bangkit2023.isnangram.databinding.ItemListStoryBinding
import com.bangkit2023.isnangram.main.ui.detail.DetailActivity
import com.bangkit2023.isnangram.utils.Const.EXTRA_DETAIL
import com.bangkit2023.isnangram.utils.Helper

@SuppressLint("ObsoleteSdkInt")
@RequiresApi(Build.VERSION_CODES.M)
class StoryAdapter : PagingDataAdapter<StoryEntity, StoryAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private val binding: ItemListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, story: StoryEntity) {
            val time = "🕓 Uploaded ${Helper.getTimelineUpload(context, story.createdAt)}"
            with(binding) {
                tvItemName.text = story.name
                storyUploadTime.text = time

                val loading = CircularProgressDrawable(context)
                loading.setColorSchemeColors(
                    context.getColor(R.color.teal_200),
                    context.getColor(R.color.teal_700),
                    context.getColor(R.color.gray)
                )
                loading.centerRadius = 30f
                loading.strokeWidth = 5f
                loading.start()

                Glide.with(context)
                    .load(story.photoUrl)
                    .placeholder(loading)
                    .into(ivItemPhoto)

                root.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            root.context as Activity,
                            Pair(tvItemName, "name"),
                            Pair(cardview, "image")
                        )
                    Intent(context, DetailActivity::class.java).also { intent ->
                        intent.putExtra(EXTRA_DETAIL, story)
                        context.startActivity(intent, optionsCompat.toBundle())
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(holder.itemView.context, story)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemListStoryBinding =
            ItemListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemListStoryBinding)
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
