package com.example.postfeed.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postfeed.R
import com.example.postfeed.model.PostListData
import com.example.postfeed.model.PostResponseModel
import com.example.postfeed.repository.PostRepository
import com.example.postfeed.utils.Utils
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

class PostViewModel(private val apiRepository: PostRepository) : ViewModel() {

    private val _isInternet = MutableLiveData<Boolean>(true)
    val isInternet: LiveData<Boolean> = _isInternet

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    val pageNo: MutableLiveData<Int> = MutableLiveData(1) // Initialize with default value 1

    private val _posts = MutableLiveData<List<PostListData>>()
    val posts: LiveData<List<PostListData>> = _posts

    private val _allPostData = MutableLiveData<PostResponseModel>()
    val allPostData: LiveData<PostResponseModel> = _allPostData

    private val allPosts = mutableListOf<PostListData>()

    fun callgetPostApi(context: Context, reset: Boolean = false) {
        if (!Utils.isInternetAvailable(context)) {
            _isInternet.value = false
            return
        }

        if (reset) {
            pageNo.value = 1
            allPosts.clear()
            _posts.value = emptyList()
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val page = pageNo.value ?: 1
                val response = apiRepository.getPostData(Utils.token, page)
                if (response.status == "200") {
                    _allPostData.value = response
                    val imagePosts = response.data.filter { postListData ->
                        postListData.post != null && postListData.post?.media?.any { it.type == "Image" } ?: false
                    } ?: emptyList()
                    allPosts.addAll(imagePosts)
                    _posts.value = allPosts.toList() // Update LiveData with accumulated posts
                    if (imagePosts.isNotEmpty()) {
                        pageNo.value = page + 1
                    }
                } else {
                    _error.value = response.message ?: "Server error"
                }
            } catch (e: HttpException) {
                e.printStackTrace()
                _error.value = "HTTP error: ${e.code()} - ${e.message()}"
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
                _error.value = "Parsing error: ${e.message}"
            } catch (e: UnknownHostException) {
                e.printStackTrace()
                _error.value = context.resources.getString(R.string.network_error)
            } catch (e: IOException) {
                e.printStackTrace()
                _error.value = context.resources.getString(R.string.network_error)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("PostViewModel", "Unexpected error: ${e.javaClass.simpleName} - ${e.message}")
                _error.value = "Unexpected error: ${e.javaClass.simpleName} - ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

}