package com.example.eazyffmpeg

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class activity_compressTest{

    @get: Rule
    val activityRule: ActivityScenarioRule<activity_compress> = ActivityScenarioRule(activity_compress::class.java)

    @Test
    fun co_isActivityInView() {

        //check if main activity gets displayed
        Espresso.onView(ViewMatchers.withId(R.id.compress)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}