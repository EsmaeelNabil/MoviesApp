package com.esmaeel.moviesapp.ui.PersonDetailsPage

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.esmaeel.moviesapp.R
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class PersonDetailsActivityTest {

    @Test
    fun testActivity_is_Started_and_showing_to_the_user(){
        val activityScenario = ActivityScenario.launch(PersonDetailsActivity::class.java)
        onView(withId(R.id.topPart)).check(matches(isDisplayed()))
    }

}