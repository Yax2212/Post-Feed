package com.example.postfeed.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.postfeed.R
import com.example.postfeed.adapter.PostAdapter
import com.example.postfeed.api.RetrofitInstance
import com.example.postfeed.databinding.ActivityPostBinding
import com.example.postfeed.repository.PostRepository
import com.example.postfeed.viewModel.PostViewModel

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    private lateinit var viewModel: PostViewModel
    private lateinit var adapter: PostAdapter
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.shimmerLayout.startShimmer()
        val postRepository = PostRepository(RetrofitInstance.apiService)
        viewModel = PostViewModel(postRepository)

        setClickListener()
        setupRecyclerView()
        observeViewModel()
        viewModel.callgetPostApi(this@PostActivity)
    }

    private fun setClickListener() {
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun observeViewModel() {
        viewModel.posts.observe(this) { posts ->
            adapter.submitList(posts)
            isLoading = false
            adapter.setLoading(false)
            binding.shimmerLayout.isVisible = false
        }

        viewModel.isLoading.observe(this) { loading ->
            isLoading = loading
            adapter.setLoading(loading && viewModel.pageNo.value!! > 1)
            binding.shimmerLayout.isVisible = loading && viewModel.pageNo.value!! == 1
            if (loading && viewModel.pageNo.value!! == 1) {
                binding.shimmerLayout.startShimmer()
            } else {
                binding.shimmerLayout.stopShimmer()
            }
        }

        viewModel.error.observe(this) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isInternet.observe(this) { isInternet ->
            if (!isInternet) {
                Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = PostAdapter { postId, position ->
            //use for api call
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@PostActivity)
            adapter = this@PostActivity.adapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy <= 0 || isLoading) return

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 5
                        && firstVisibleItemPosition >= 0
                    ) {
                        isLoading = true
                        (adapter as PostAdapter).setLoading(true)
                        viewModel.callgetPostApi(this@PostActivity)
                    }
                }
            })
        }
    }
}