package com.esmaeel.moviesapp.di

import com.esmaeel.moviesapp.BuildConfig
import com.esmaeel.moviesapp.Utils.Constants
import com.esmaeel.moviesapp.Utils.MyUtils
import com.esmaeel.moviesapp.Utils.isJson
import com.esmaeel.moviesapp.data.remote.MoviesNetworkService
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {


    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BaseUrl

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class API_KEY


    @Provides
    @Singleton
    @BaseUrl
    fun provideBaseUrl(): String = "https://api.themoviedb.org/3/"

    @Provides
    @Singleton
    @API_KEY
    fun provideApiKey(): String = "0a4b834208c248be5e926aa56b23e6da"


    @Provides
    @Singleton
    fun provideAuthInterceptor(@API_KEY apiKey: String): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            chain.proceed(
                chain.request().newBuilder()
                    .url(MyUtils.injectApiKey(original, apiKey))
                    .method(original.method, original.body)
                    .build()
            )
        }
    }


    /**
     * @return a Singleton HttpLoggingInterceptor with Beauty Json logger
     *  That Log our network data only in Debug mode.
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return if (BuildConfig.DEBUG) HttpLoggingInterceptor(object :
            HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                if (message.isJson())
                    Logger.t(Constants.JR).json(message)
                else Logger.t(Constants.OK_HTTP_MESSAGE_LOGGER).i(message)
            }

        }).setLevel(
            HttpLoggingInterceptor.Level.BODY
        ) else HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.NONE
        )
    }


    /**
     * @param logger HttpLoggingInterceptor
     * @param localInterceptor Interceptor
     * @return OkHttpClient with Auth injector and Logger Interceptors.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        logger: HttpLoggingInterceptor,
        authInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logger)
            .build()
    }


    /**
     * @param okHttpClient OkHttpClient with Auth and logger interceptors
     * @param baseUrl String depending on if the app is (Debug - Release).
     * @return MoviesNetworkService : Network gateway
     */
    @Provides
    @Singleton
    fun provideMoviesNetworkService(
        okHttpClient: OkHttpClient,
        @BaseUrl baseUrl: String
    ): MoviesNetworkService {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            /*.addCallAdapterFactory(RxJava2CallAdapterFactory.create())*/
            .baseUrl(baseUrl)
            .build()
            .create(MoviesNetworkService::class.java)
    }

}