package com.example.postfeed.api

import com.example.postfeed.model.PostResponseModel
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("post/getPost")
    suspend fun getPostData(
        @Header("Authorization") token: String,
        @Field("page") page: Int
    ): PostResponseModel

}