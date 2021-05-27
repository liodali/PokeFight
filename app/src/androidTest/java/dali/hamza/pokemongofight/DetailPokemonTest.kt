package dali.hamza.pokemongofight


import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import androidx.core.app.ComponentActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
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
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailPokemonTest {

    private lateinit var scenario: ActivityScenario<DetailPokemonActivity>

    private lateinit var intent: Intent


    @get:Rule
    var activityScenarioRule = ActivityScenarioRule<DetailPokemonActivity>(
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
    )

    @After
    fun cleanup() {
        scenario.close()
    }

    @Test
    fun testMyTeam() {
        intent =
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
        scenario = activityScenarioRule.scenario
        scenario.moveToState(Lifecycle.State.CREATED)
        var title = ""
        scenario.onActivity {
            title = it.title.toString()
        }
        assert(title == "balbosoura")

        scenario.moveToState(Lifecycle.State.RESUMED)
        onView(withId(R.id.id_captured_pokemon_fl_bt)).check(matches(isDisplayed()))

    }

    @Test
    fun testWild() {

        intent = Intent(
            ApplicationProvider.getApplicationContext(), DetailPokemonActivity::class.java
        ).apply {
            this.putExtras(
                bundleOf(
                    DetailPokemonActivity.keyPoke to PokemonWithGeoPoint(
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
                    DetailPokemonActivity.keyTypeDetail to "wild"
                )
            )
        }
        scenario = ActivityScenario.launch(
            intent
        )

        scenario.moveToState(Lifecycle.State.CREATED)
        var title = ""
        scenario.onActivity {
            title = it.title.toString()
        }
        assert(title == "balbosoura")

        scenario.moveToState(Lifecycle.State.RESUMED)
        onView(withId(R.id.id_captured_pokemon_fl_bt)).check{ view, noViewFoundException ->
            assert(!view.isVisible)
        }
        onView(withId(R.id.id_bt_capture_pokemon)).check { view, noViewFoundException ->
            assert(view.isVisible)
        }

    }
}