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
class activity_textTest{

    @get: Rule
    val activityRule: ActivityScenarioRule<activity_text> = ActivityScenarioRule(activity_text::class.java)

    @Test
    fun te_isActivityInView() {

        //check if main activity gets displayed
        onView(withId(R.id.text)).check(matches(isDisplayed()))
    }

    @Test
    fun te_UIElementsLoadProperly() {
        //Header
        onView(withId(R.id.headerIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.headerText)).check(matches(isDisplayed()))

        //Input Card
        onView(withId(R.id.inputIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.inputText)).check(matches(isDisplayed()))
        onView(withId(R.id.txtFilePath)).check(matches(isDisplayed()))
        onView(withId(R.id.btnFileDialog)).check(matches(isDisplayed()))

        //Text
        onView(withId(R.id.textIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.textDisplay)).check(matches(isDisplayed()))
        onView(withId(R.id.txtInputText)).check(matches(isDisplayed()))

        //Color
        onView(withId(R.id.colorIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.colorDisplay)).check(matches(isDisplayed()))
        onView(withId(R.id.colorSpinner)).check(matches(isDisplayed()))

        //Font
        onView(withId(R.id.fontIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.fontDisplay)).check(matches(isDisplayed()))
        onView(withId(R.id.sizeSpinner)).check(matches(isDisplayed()))

        //Location
        onView(withId(R.id.locationIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.locationDisplay)).check(matches(isDisplayed()))
        onView(withId(R.id.xDisplay)).check(matches(isDisplayed()))
        onView(withId(R.id.yDisplay)).check(matches(isDisplayed()))
        onView(withId(R.id.txtXInput)).check(matches(isDisplayed()))
        onView(withId(R.id.txtYInput)).check(matches(isDisplayed()))

        //Process Info
        onView(withId(R.id.processIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.processText)).check(matches(isDisplayed()))
        //onView(withId(R.id.txtInfo)).check(matches(isDisplayed()))

        //ActionButton
        //onView(withId(R.id.btnAddTextToVideo)).check(matches(isDisplayed()))
    }
}