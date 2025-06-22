package com.example.postfeed.repository

import com.example.postfeed.api.ApiService
import com.example.postfeed.model.PostResponseModel

class PostRepository(private val apiService: ApiService) {

    suspend fun getPostData(token: String, page: Int): PostResponseModel {
        return apiService.getPostData("Bearer $token", page)
    }
}