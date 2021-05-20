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
class activity_effectsTest{

    @get: Rule
    val activityRule: ActivityScenarioRule<activity_effects> = ActivityScenarioRule(activity_effects::class.java)

    @Test
    fun ef_isActivityInView() {

        //check if main activity gets displayed
        onView(withId(R.id.effects)).check(matches(isDisplayed()))
    }

    @Test
    fun ef_UIElementsLoadProperly() {
        //Header
        onView(withId(R.id.headerIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.headerText)).check(matches(isDisplayed()))

        //Input Card
        onView(withId(R.id.inputIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.inputText)).check(matches(isDisplayed()))
        onView(withId(R.id.txtFilePath)).check(matches(isDisplayed()))
        onView(withId(R.id.btnFileDialog)).check(matches(isDisplayed()))

        //Effects
        onView(withId(R.id.effectsIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.effectsText)).check(matches(isDisplayed()))
        onView(withId(R.id.effectsSpinner)).check(matches(isDisplayed()))

        //Process Info
        onView(withId(R.id.processIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.processText)).check(matches(isDisplayed()))
        onView(withId(R.id.txtInfo)).check(matches(isDisplayed()))

        //ActionButton
        onView(withId(R.id.btnApplyEffect)).check(matches(isDisplayed()))
    }
}