package com.esmaeel.moviesapp.di

import com.esmaeel.moviesapp.ui.PopularPersonsPage.Adapter.PopularPersonsAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object MainModule {

    @Provides
    fun providePopularPersonsAdapter(
        @PROFILE_IMAGE_BASE_URL baseUrl: String
    ): PopularPersonsAdapter {
        return PopularPersonsAdapter(baseUrl)
    }

}