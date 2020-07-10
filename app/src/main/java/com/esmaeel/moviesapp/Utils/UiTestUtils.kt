package com.esmaeel.moviesapp.Utils

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.idling.CountingIdlingResource

/*
* Helps the ui to wait for a network request in espresso ui tests
* */
object UiTestUtils {

    private const val RESOURCE = "GLOBAL"

    @JvmField val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun waiT() {
        countingIdlingResource.increment()
    }

    fun release() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}




