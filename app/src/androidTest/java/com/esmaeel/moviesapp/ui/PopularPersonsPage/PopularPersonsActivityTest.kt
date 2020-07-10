package com.esmaeel.moviesapp.ui.PopularPersonsPage

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.esmaeel.moviesapp.R
import com.esmaeel.moviesapp.Utils.CoroutinesManager
import com.esmaeel.moviesapp.Utils.UiTestUtils
import com.esmaeel.moviesapp.clickItemWithId
import com.esmaeel.moviesapp.ui.PopularPersonsPage.Adapter.PopularPersonsAdapter
import org.hamcrest.CoreMatchers.not
import org.junit.*
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class PopularPersonsActivityTest {

    val ITEM_POSITION = 1

    @get:Rule
    val activityRule = ActivityScenarioRule(PopularPersonsActivity::class.java)


    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(UiTestUtils.countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(UiTestUtils.countingIdlingResource)
    }


    @Test
    fun testActivity_is_Started_and_showing_to_the_user() {
        activityRule.scenario
        onView(withId(R.id.topbar)).check(matches(isDisplayed()))
    }


    /**
     *  get instance of the recyclerview from the activity
     *  sleep for 1 second for the network call
     *  get items count
     *  scroll to the bottom
     *  get new items count
     *
     *  check if not equal.
     */
    @Test
    fun wait_for_network_results_and_test_scrolling_to_see_it_gets_new_items_after_pagination() {
        var recycler: RecyclerView? = null
        activityRule.scenario.onActivity {
            recycler = it.findViewById(R.id.recycler)
        }

        Thread.sleep(1000)

        val itemcount = recycler?.adapter?.itemCount
        Assert.assertNotNull("itemcount : $itemcount", itemcount)

        CoroutinesManager.onMainThread {
            recycler?.scrollToPosition(itemcount!! - 1)
        }

        Thread.sleep(2000)
        val secondItemCount = recycler?.adapter?.itemCount
        Assert.assertNotSame("new itemcount : $secondItemCount", itemcount, secondItemCount)


    }


    @Test
    fun wait_for_network_results_and_open_person_details_page() {
        activityRule.scenario
        /*Make sure person's recycler is showing*/
        onView(withId(R.id.recycler)).check(matches(isDisplayed()));
        /* we did this because countingIdlingResource doesn't wait enough for the recycler to bind its views
           Wait a second for the recycler view to bind its views if the network connection is good*/
        Thread.sleep(1000)
        onView(withId(R.id.recycler))
            .perform(
                actionOnItemAtPosition<PopularPersonsAdapter.PersonHolder>(
                    ITEM_POSITION,
                    clickItemWithId(R.id.person_image)
                )
            )
    }


    @Test
    fun wait_for_network_results_and_open_person_details_page_and_check_for_person_data() {
        activityRule.scenario
        /*Make sure person's recycler is showing*/
        onView(withId(R.id.recycler)).check(matches(isDisplayed()));

        /* we did this because countingIdlingResource doesn't wait enough for the recycler to bind its views
           Wait a second for the recycler view to bind its views if the network connection is good*/
        Thread.sleep(1000)

        // Click on the recycler item
        onView(withId(R.id.recycler))
            .perform(
                actionOnItemAtPosition<PopularPersonsAdapter.PersonHolder>(
                    ITEM_POSITION,
                    clickItemWithId(R.id.person_image)
                )
            )

        // make sure person name text is visible
        onView(withId(R.id.name)).check(matches(isDisplayed()))

        // make sure person name is not empty and the data is passed successfully
        onView(withId(R.id.name)).check(matches(not(withText(""))));


    }
}

