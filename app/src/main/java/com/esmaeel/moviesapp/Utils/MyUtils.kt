package com.esmaeel.moviesapp.Utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.esmaeel.moviesapp.data.models.ErrorModel
import com.esmaeel.moviesapp.data.models.PopularPersonsResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.PrettyFormatStrategy
import okhttp3.Request
import okhttp3.ResponseBody
import timber.log.Timber
import java.util.*


object MyUtils {

    fun injectApiKey(original: Request, apiKey: String) = original.url.newBuilder()
        .addQueryParameter("api_key", apiKey)
        .build()

    fun getFormatter(): FormatStrategy {
        return PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .methodCount(0)
            .methodOffset(5)
            .tag(Constants.GLOBAL_LOGGER_)
            .build()
    }

    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    fun getErrorFromBody(errorBody: ResponseBody?): String {
        val type = object : TypeToken<ErrorModel>() {}.type
        var errorResponse: ErrorModel? =
            Gson().fromJson(errorBody?.charStream(), type) ?: (ErrorModel())

        /*For Pages Error in 422 status code*/
        if (!errorResponse?.errors.isNullOrEmpty())
            return errorResponse?.errors.toString() ?: ""

        /*For Pages Error in 401 status code*/
        return errorResponse?.status_message ?: ""

    }

    // TODO: 7/9/20 test this
    fun getKnownFor(knownFor: ArrayList<PopularPersonsResponse.Result.KnownFor?>?): String {
        var knownForString = "Known For : "
        knownFor?.let {
            it.forEach { knownFor ->
                if (knownFor?.original_title?.isNotEmpty()!!)
                    knownForString += "${knownFor?.original_title}, "
            }
        }

        Timber.e(knownForString)
        return knownForString.dropLast(2)
    }




}