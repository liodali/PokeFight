package dali.hamza.pokemongofight


import android.app.Instrumentation
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ComponentActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.material.appbar.AppBarLayout
import dali.hamza.core.utilities.DateManager
import dali.hamza.domain.models.PokeGeoPoint
import dali.hamza.domain.models.Pokemon
import dali.hamza.domain.models.PokemonWithGeoPoint
import dali.hamza.pokemongofight.ui.activity.DetailPokemonActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailPokemonTest {

    val intent =
        Intent(ApplicationProvider.getApplicationContext(), DetailPokemonActivity::class.java)
            .putExtras(
                bundleOf(
                    DetailPokemonActivity.keyMePoke to PokemonWithGeoPoint(
                        pokemon = Pokemon(
                            id = 1,
                            name = "balbosoura",
                            url = "https://pokeapi.co/api/v2/pokemon/1/",
                            captured_at = DateManager.dateFormat_full.format(DateManager.now())
                        ),
                        pokeGeoPoint = PokeGeoPoint(
                            lat = 0.0,
                            lon = 0.0
                        )
                    ),
                    DetailPokemonActivity.keyTypeDetail to "me"
                )
            )

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule<DetailPokemonActivity>(
        intent
    )


    @Test
    fun testEvent() {

        val scenario = activityScenarioRule.scenario
        scenario.moveToState(Lifecycle.State.CREATED)
        var title = ""
        scenario.onActivity {
            title = it.title.toString()
        }
        assert(title == "balbosoura")

        scenario.moveToState(Lifecycle.State.RESUMED)
        onView(withId(R.id.id_captured_pokemon_fl_bt)).check(matches(isDisplayed()))

    }
}