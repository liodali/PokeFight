package dali.hamza.pokemongofight.di

import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dali.hamza.core.repository.PokemonRepository
import dali.hamza.domain.repository.IPokemonRepository

@InstallIn(
    ActivityComponent::class,
    FragmentComponent::class,
    ViewModelComponent::class
)
class PokemonModule {
    @Provides
    fun providePokemonRepository(repository: PokemonRepository): IPokemonRepository =
        repository
}