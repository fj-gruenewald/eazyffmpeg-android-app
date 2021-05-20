package com.example.eazyffmpeg

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class activity_creationTest{

    @get: Rule
    val activityRule: ActivityScenarioRule<activity_creation> = ActivityScenarioRule(activity_creation::class.java)

    @Test
    fun cr_isActivityInView() {

        //check if main activity gets displayed
        onView(withId(R.id.creation)).check(matches(isDisplayed()))
    }

    @Test
    fun cr_UIElementsLoadProperly() {
        //Header
        onView(withId(R.id.headerIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.headerText)).check(matches(isDisplayed()))

        //Input Video Card
        onView(withId(R.id.inputVideoIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.inputVideoText)).check(matches(isDisplayed()))
        onView(withId(R.id.txtFilePath)).check(matches(isDisplayed()))
        onView(withId(R.id.btnFileDialog)).check(matches(isDisplayed()))

        //Input Image Card
        onView(withId(R.id.inputImageIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.inputImageText)).check(matches(isDisplayed()))
        onView(withId(R.id.txtImageFilePath)).check(matches(isDisplayed()))
        onView(withId(R.id.btnImageFileDialog)).check(matches(isDisplayed()))

        //Process Info
        onView(withId(R.id.processIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.processText)).check(matches(isDisplayed()))
        onView(withId(R.id.txtInfo)).check(matches(isDisplayed()))

        //ActionButton
        onView(withId(R.id.btnCreateVideo)).check(matches(isDisplayed()))
    }
}