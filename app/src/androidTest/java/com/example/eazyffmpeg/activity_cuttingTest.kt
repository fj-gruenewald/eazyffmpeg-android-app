package com.example.eazyffmpeg

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class activity_cuttingTest {

    @get: Rule
    val activityRule: ActivityScenarioRule<activity_cutting> =
        ActivityScenarioRule(activity_cutting::class.java)

    @Test
    fun cu_isActivityInView() {

        //check if main activity gets displayed
        onView(withId(R.id.cutting)).check(matches(isDisplayed()))
    }

    @Test
    fun cu_UIElementsLoadProperly() {
        //Header
        onView(withId(R.id.headerIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.headerText)).check(matches(isDisplayed()))

        //Input Card
        onView(withId(R.id.inputIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.inputText)).check(matches(isDisplayed()))
        onView(withId(R.id.txtFilePath)).check(matches(isDisplayed()))
        onView(withId(R.id.btnFileDialog)).check(matches(isDisplayed()))

        //Time Component
        onView(withId(R.id.displayFrom)).check(matches(isDisplayed()))
        onView(withId(R.id.txtFrom)).check(matches(isDisplayed()))
        onView(withId(R.id.btnFrom)).check(matches(isDisplayed()))
        onView(withId(R.id.displayTo)).check(matches(isDisplayed()))
        onView(withId(R.id.txtTo)).check(matches(isDisplayed()))
        onView(withId(R.id.btnTo)).check(matches(isDisplayed()))

        //Process Info
        onView(withId(R.id.processIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.processText)).check(matches(isDisplayed()))
        onView(withId(R.id.txtInfo)).check(matches(isDisplayed()))

        //ActionButton
        onView(withId(R.id.btnCutting)).check(matches(isDisplayed()))
    }
}