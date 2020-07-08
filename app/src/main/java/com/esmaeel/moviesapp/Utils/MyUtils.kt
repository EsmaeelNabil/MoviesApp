package com.esmaeel.moviesapp.Utils

import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.PrettyFormatStrategy
import okhttp3.Request

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


}