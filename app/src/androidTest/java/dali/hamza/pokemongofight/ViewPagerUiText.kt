package dali.hamza.pokemongofight

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.android.material.tabs.TabLayout
import dali.hamza.pokemongofight.ui.activity.MainActivity
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ViewPagerUiText {

    private lateinit var communityLabel: String
    private lateinit var capturedLabel: String

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun initValidString() {
        // Specify a valid string.
        communityLabel = "Community"
        capturedLabel = "Captured"
    }

    @Test
    fun selectCommunity() {
        // Type text and then press the button.

        onView(withText(communityLabel))
            .perform(click())

        onView(withText("fragCommunity"))
            .check(matches(isDisplayed()))

        onView(allOf(withId(R.id.idm_main_tab_layout)))
            .perform(selectTabAtPosition(3))
        try {
            Thread.sleep(200)
        } catch (e: Exception) {

        }
        onView(withText("fragCaptured"))
            .check(matches(isDisplayed()))
    }

    private fun selectTabAtPosition(tabIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "with tab at index $tabIndex"

            override fun getConstraints() =
                allOf(isDisplayed(), isAssignableFrom(TabLayout::class.java))

            override fun perform(uiController: UiController, view: View) {
                val tabLayout = view as TabLayout
                val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex)
                    ?: throw PerformException.Builder()
                        .withCause(Throwable("No tab at index $tabIndex"))
                        .build()

                tabAtIndex.select()
            }
        }
    }
}