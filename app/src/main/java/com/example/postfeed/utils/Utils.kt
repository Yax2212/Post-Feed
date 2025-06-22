package com.example.postfeed.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class Utils {

    companion object {

        const val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyRGF0YSI6eyJfaWQiOiI2MTEiLCJuYW1lIjoiTWFuaWoiLCJjb3VudHJ5Q29kZSI6Iis5MSIsImVtYWlsIjoiIiwicGhvbmUiOjk4NzkxNjk3ODgsImRlc2NyaXB0aW9uIjoiSGV5LCBJJ20gaW4gVGVwbm90IiwidGFncyI6IiIsInByb2ZpbGUiOiJ1cGxvYWRzL3Byb2ZpbGUvODM5NUZCMjQtQ0NDQS00MzRFLUI5NTYtNDY1ODA3QTUyNkUzLTI2MzgtMDAwMDA1NkYyNjMyOEM3Ri5qcGVnIiwib3RwU2VuZENvdW50IjoxLCJsYXN0T3RwU2VuZERhdGUiOiIyMDI1LTAxLTA3VDA4OjMyOjAyLjE0NloiLCJpc1VzZXJWZXJpZmllZCI6dHJ1ZSwiaXNCYW5uZWQiOmZhbHNlLCJkYXRlT2ZCaXJ0aCI6IjE5ODAtMDEtMDFUMDA6MDA6MDAuMDAwWiIsImxpbmsiOiIiLCJpc0RlbGV0ZWQiOmZhbHNlLCJvbGRQaG9uZSI6bnVsbCwidXNlclR5cGUiOiJVc2VyIiwiY3JlYXRlQXQiOiIyMDI0LTEwLTA4VDA1OjA2OjQwLjMwMFoiLCJ1cGRhdGVBdCI6IjIwMjUtMDEtMDdUMDg6MzI6MzkuMzQzWiIsImJpb1VwZGF0ZSI6IjIwMjUtMDEtMDdUMDg6MzI6MzkuMzQzWiIsIl9fdiI6MCwiaXNPdHBWZXJpZmllZCI6dHJ1ZX0sImlhdCI6MTc0ODQxNDU2Mn0.pZ7p06NGGbQOfN8mHVqX0OdzgFejlRjooTwLz2UUqLg"


        fun getDate(input: String): String {
            return try {
                val utcFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                utcFormat.timeZone = TimeZone.getTimeZone("UTC")

                val date = utcFormat.parse(input)

                val localFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                localFormat.format(date!!)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }


        fun isInternetAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return false

            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
    }
}