package com.grandefirano.rickandmortycharacterfinder


import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun navigationTest() {
        val recyclerView = onView(withId(R.id.characterList))
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))



        val textView = onView(withId(R.id.portalTextView))
        textView.check(matches(withText("More Info")))

        val imageView = onView(withId(R.id.statusImageView))
        imageView.check(matches(isDisplayed()))

        val imageView2 = onView(withId(R.id.genderImageView))
        imageView2.check(matches(isDisplayed()))

        val imageView3 = onView(withId(R.id.portalImageView))

        imageView3.check(matches(isDisplayed()))

        val imageView4 = onView(withId(R.id.bornPlaceImageView))
        imageView4.check(matches(isDisplayed()))

        val imageView5 = onView(withId(R.id.presentPlaceImageView))
        imageView5.check(matches(isDisplayed()))

        val textView2 = onView(withId(R.id.nameTextView))
        textView2.check(matches(isDisplayed()))

        val imageView6 = onView(withId(R.id.photoDetailsImageView))
        imageView6.check(matches(isDisplayed()))

        val appCompatImageView = onView(withId(R.id.portalImageView))
        appCompatImageView.perform(click())

        val imageView7 = onView(withId(R.id.photoDetailsImageView))
        imageView7.check(matches(isDisplayed()))

        val textView3 = onView(withId(R.id.nameTextView))
        textView3.check(matches(isDisplayed()))

        val imageView8 = onView(withId(R.id.genderImageView))
        imageView8.check(matches(isDisplayed()))

        val textView4 = onView(withId(R.id.genderTitleTextView))
        textView4.check(matches(withText("Gender")))

        val textView5 = onView(withId(R.id.genderTextView))
        textView5.check(matches(isDisplayed()))

        val imageView9 = onView(withId(R.id.statusImageView))
        imageView9.check(matches(isDisplayed()))

        val textView6 = onView(withId(R.id.statusTitleTextView))
        textView6.check(matches(withText("Alive or no?")))

        val textView7 = onView(withId(R.id.statusTextView))
        textView7.check(matches(isDisplayed()))

        val imageView10 = onView(withId(R.id.bornPlaceImageView))
        imageView10.check(matches(isDisplayed()))

        val textView8 = onView(withId(R.id.bornPlaceTitleTextView))
        textView8.check(matches(withText("Origin Location")))

        val textView9 = onView(withId(R.id.bornPlaceTextView))
        textView9.check(matches(isDisplayed()))

        val imageView11 = onView(withId(R.id.presentPlaceImageView))
        imageView11.check(matches(isDisplayed()))

        val textView10 = onView(withId(R.id.presentPlaceTitleTextView))
        textView10.check(matches(withText("Present Location")))

        val textView11 = onView(withId(R.id.presentPlaceTextView))
        textView11.check(matches(isDisplayed()))

        val appCompatImageView2 = onView(withId(R.id.photoDetailsImageView))
        appCompatImageView2.perform(click())

        val textView12 = onView(withId(R.id.portalTextView))
        textView12.check(matches(isDisplayed()))

        val imageView12 = onView(withId(R.id.portalImageView))
        imageView12.check(matches(isDisplayed()))
    }

}
