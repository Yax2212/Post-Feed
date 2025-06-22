package com.example.postfeed.api

import android.util.Log
import com.example.postfeed.utils.Utils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private const val BASE_URL = "http://43.205.16.96:3000/api/v2/"

    private val retrofit: Retrofit by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(HttpStatusInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

}
class HttpStatusInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val token = Utils.token

        val requestBuilder = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")

        requestBuilder.addHeader("Token", token ?: "")
        Log.e("TAG", "Token ::: $token")

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
