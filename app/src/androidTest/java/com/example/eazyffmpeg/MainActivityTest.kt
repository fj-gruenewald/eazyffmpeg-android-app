package com.example.eazyffmpeg

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @get: Rule
    val activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun ma_isActivityInView() {

        //check if main activity gets displayed
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }

    @Test
    fun ma_UIElementsLoadProperly() {

        //Headline
        onView(withId(R.id.textView)).check(matches(isDisplayed()))
        onView(withId(R.id.textView1)).check(matches(isDisplayed()))

        onView(withId(R.id.textView)).check(matches(withText(R.string.mainActivityHeadline)))
        onView(withId(R.id.textView1)).check(matches(withText(R.string.mainActivityDescriptor)))

        //Main Image
        onView(withId(R.id.mainImage)).check(matches(isDisplayed()))

        //Button Creation
        onView(withId(R.id.videoCreation)).check(matches(isDisplayed()))
        onView(withId(R.id.videoCreationIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.videoCreationText)).check(matches(isDisplayed()))

        onView(withId(R.id.videoCreationText)).check(matches(withText(R.string.menuItemVideoCreation)))

        //Button Cutting
        onView(withId(R.id.videoCutting)).check(matches(isDisplayed()))
        onView(withId(R.id.videoCuttingIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.videoCuttingText)).check(matches(isDisplayed()))

        onView(withId(R.id.videoCuttingText)).check(matches(withText(R.string.menuItemVideoCutting)))

        //Button Compress
        onView(withId(R.id.videoCompress)).check(matches(isDisplayed()))
        onView(withId(R.id.videoCompressIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.videoCompressText)).check(matches(isDisplayed()))

        onView(withId(R.id.videoCompressText)).check(matches(withText(R.string.menuItemVideoCompress)))

        //Button Text
        onView(withId(R.id.videoTextAndTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.videoTextAndTitleIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.videoTextAndTitleText)).check(matches(isDisplayed()))

        onView(withId(R.id.videoTextAndTitleText)).check(matches(withText(R.string.menuItemVideoText)))

        //Button Effects
        onView(withId(R.id.videoEffects)).check(matches(isDisplayed()))
        onView(withId(R.id.videoEffectsIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.videoEffectsText)).check(matches(isDisplayed()))

        onView(withId(R.id.videoEffectsText)).check(matches(withText(R.string.menuItemVideoEffects)))

        //Button Crop
        onView(withId(R.id.videoCropping)).check(matches(isDisplayed()))
        onView(withId(R.id.videoCroppingIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.videoCroppingText)).check(matches(isDisplayed()))

        onView(withId(R.id.videoCroppingText)).check(matches(withText(R.string.menuItemVideoTransform)))

        //Button Theme
        onView(withId(R.id.btnDayNight)).check(matches(isDisplayed()))
    }

    @Test
    fun ma_navigationTest_VideoCreation() {
        //Button Creation
        onView(withId(R.id.videoCreation)).perform(click())
        onView(withId(R.id.creation)).check(matches(isDisplayed()))
    }

    @Test
    fun ma_navigationTest_VideoCutting() {
        //Button Cutting
        onView(withId(R.id.videoCutting)).perform(click())
        onView(withId(R.id.cutting)).check(matches(isDisplayed()))
    }

    @Test
    fun ma_navigationTest_VideoCompress() {
        //Button Compress
        onView(withId(R.id.videoCompress)).perform(click())
        onView(withId(R.id.compress)).check(matches(isDisplayed()))
    }

    @Test
    fun ma_navigationTest_TextAndTitle() {
        //Button Text
        onView(withId(R.id.videoTextAndTitle)).perform(click())
        onView(withId(R.id.text)).check(matches(isDisplayed()))
    }

    @Test
    fun ma_navigationTest_VideoEffects() {
        //Button Effects
        onView(withId(R.id.videoEffects)).perform(click())
        onView(withId(R.id.effects)).check(matches(isDisplayed()))
    }

    @Test
    fun ma_navigationTest_VideoTransform() {
        //Button Crop
        onView(withId(R.id.videoCropping)).perform(click())
        onView(withId(R.id.transform)).check(matches(isDisplayed()))
    }

    @Test
    fun ma_navigationTest_NavigateBackAndContinue() {

        //Button Creation
        onView(withId(R.id.videoCreation)).perform(click())
        onView(withId(R.id.creation)).check(matches(isDisplayed()))

        pressBack()
        onView(withId(R.id.main)).check(matches(isDisplayed()))

        //Button Cutting
        onView(withId(R.id.videoCutting)).perform(click())
        onView(withId(R.id.cutting)).check(matches(isDisplayed()))

        pressBack()
        onView(withId(R.id.main)).check(matches(isDisplayed()))

        //Button Compress
        onView(withId(R.id.videoCompress)).perform(click())
        onView(withId(R.id.compress)).check(matches(isDisplayed()))

        pressBack()
        onView(withId(R.id.main)).check(matches(isDisplayed()))

        //Button Text
        onView(withId(R.id.videoTextAndTitle)).perform(click())
        onView(withId(R.id.text)).check(matches(isDisplayed()))

        pressBack()
        onView(withId(R.id.main)).check(matches(isDisplayed()))

        //Button Effects
        onView(withId(R.id.videoEffects)).perform(click())
        onView(withId(R.id.effects)).check(matches(isDisplayed()))

        pressBack()
        onView(withId(R.id.main)).check(matches(isDisplayed()))

        //Button Crop
        onView(withId(R.id.videoCropping)).perform(click())
        onView(withId(R.id.transform)).check(matches(isDisplayed()))
    }
}