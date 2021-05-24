package dali.hamza.pokemongofight.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dali.hamza.core.repository.AppRepository
import dali.hamza.core.repository.PokemonRepository
import dali.hamza.domain.repository.IAppRepository
import dali.hamza.domain.repository.IPokemonRepository

@Module
@InstallIn(
    ActivityComponent::class,
    FragmentComponent::class,
    ViewModelComponent::class
)
object AuhthorizationModule {
    @Provides
    fun provideAppRepository(repository: AppRepository): IAppRepository =
        repository
}