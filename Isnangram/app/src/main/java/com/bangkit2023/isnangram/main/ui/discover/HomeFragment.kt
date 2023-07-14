package com.bangkit2023.isnangram.main.ui.discover

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bangkit2023.isnangram.R
import com.bangkit2023.isnangram.data.local.entity.StoryEntity
import com.bangkit2023.isnangram.databinding.FragmentHomeBinding
import com.bangkit2023.isnangram.main.ui.auth.adapter.LoadingStateAdapter
import com.bangkit2023.isnangram.main.ui.auth.adapter.StoryAdapter
import com.bangkit2023.isnangram.main.ui.auth.AuthActivity
import com.bangkit2023.isnangram.main.ui.explore.StoryWithMapsActivity
import com.bangkit2023.isnangram.main.ui.main.MainActivity
import com.bangkit2023.isnangram.main.viewmodel.ViewModelFactory
import com.bangkit2023.isnangram.utils.Const
import com.bangkit2023.isnangram.utils.Helper.animateVisibility
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

@Suppress("DEPRECATION")
@OptIn(ExperimentalPagingApi::class)
@RequiresApi(Build.VERSION_CODES.M)
class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var viewModel: HomeViewModel

    private var token : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.swipeRefresh.isRefreshing = true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = binding.toolbar
        (toolbar as? Toolbar)?.title = ""
        (activity as MainActivity).setSupportActionBar(binding.toolbar)
        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        token = (activity as MainActivity).intent.getStringExtra(Const.EXTRA_TOKEN).toString()
        setAdapter()
        getAllStory(token)
        setSwipeRefreshLayout(token)
    }


    private fun setAdapter() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        storyAdapter = StoryAdapter()

        storyAdapter.addLoadStateListener { loadState ->
            if ((loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        storyAdapter.itemCount < 1) ||
                loadState.source.refresh is LoadState.Error) {
                binding.apply {
                    tvNotFoundError.animateVisibility(true)
                    ivNotFoundError.animateVisibility(true)
                    rvStories.animateVisibility(false)
                }
            } else {
                binding.apply {
                    tvNotFoundError.animateVisibility(false)
                    ivNotFoundError.animateVisibility(false)
                    rvStories.animateVisibility(true)
                }
            }
            binding.swipeRefresh.isRefreshing = loadState.source.refresh is LoadState.Loading
        }
        try {
            recyclerView = binding.rvStories
            recyclerView.apply {
                adapter = storyAdapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        storyAdapter.retry()
                    }
                )
                layoutManager = linearLayoutManager
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    private fun setSwipeRefreshLayout(token: String) {
        binding.swipeRefresh.setOnRefreshListener {
            getAllStory(token)
        }
    }

    private fun getAllStory(token: String) {
        viewModel.getStories(token).observe(viewLifecycleOwner) { result ->
            updateRecyclerViewData(result)
        }
    }

    override fun onRefresh() {
        binding.swipeRefresh.isRefreshing = true
        storyAdapter.refresh()
        Timer().schedule(2000) {
            binding.swipeRefresh.isRefreshing = false
            binding.rvStories.smoothScrollToPosition(0)
        }
    }

    private fun updateRecyclerViewData(stories: PagingData<StoryEntity>) {
        val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
        storyAdapter.submitData(lifecycle, stories)
        recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.activity_main_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.swipeRefresh -> {
                onRefresh()
            }
            R.id.map -> {
                val intent = Intent(requireContext(), StoryWithMapsActivity::class.java)
                intent.putExtra(EXTRA_KEY, token)
                startActivity(intent)
            }
            R.id.action_logout -> {
                lifecycleScope.launch {
                    viewModel.userLogout()
                }
                val intent = Intent(activity, AuthActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
        return true
    }

    companion object {
        const val EXTRA_KEY = "extra_key"
    }

}