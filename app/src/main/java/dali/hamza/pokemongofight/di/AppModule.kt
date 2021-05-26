package dali.hamza.pokemongofight.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dali.hamza.core.datasource.db.PokeAppDB
import dali.hamza.core.datasource.network.AuthorizationApi
import dali.hamza.core.datasource.network.ClientApi
import dali.hamza.core.datasource.network.PokeApiClient
import dali.hamza.core.utilities.SessionManager
import dali.hamza.pokemongofight.R
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSessionManager(application: Application): SessionManager {
        return SessionManager(application)
    }

    @Provides
    fun provideBaseUrl(application: Application) = application.getString(R.string.serverUrl)

    @Provides
    @Named("PokeApi")
    fun provideBaseUrlPokeApi(application: Application) =
        application.resources.getString(R.string.poke_api_url)

    @Provides
    @Named("EmailUser")
    fun provideEmailUser(application: Application) = application.getString(R.string.tokenEmail)

    @Provides
    fun provideMoshi(): MoshiConverterFactory = MoshiConverterFactory.create()

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        BASE_URL: String,
        moshiConverter: MoshiConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(moshiConverter)
            .build()

    @Provides
    @Singleton
    @Named("RetrofitPokeApi")
    fun provideRetrofitPokeApi(
        okHttpClient: OkHttpClient,
        @Named("PokeApi") BASE_URL_POKE_API: String,
        moshiConverter: MoshiConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL_POKE_API)
            .client(okHttpClient)
            .addConverterFactory(moshiConverter)
            .build()

    @Singleton
    @Provides
    fun provideClientApi(
        retrofit: Retrofit
    ): ClientApi = retrofit.create(ClientApi::class.java)


    @Singleton
    @Provides
    fun providePokeApiClient(
        @Named("RetrofitPokeApi")
        retrofit: Retrofit
    ): PokeApiClient =
        retrofit.create(PokeApiClient::class.java)


    @Singleton
    @Provides
    @Named("AuthorizationApi")
    fun provideAuthorizationApi(retrofit: Retrofit): AuthorizationApi =
        retrofit.create(AuthorizationApi::class.java)


    @Singleton
    @Provides
    fun provideAppDatabase(application: Application): PokeAppDB {
        return Room.databaseBuilder(application, PokeAppDB::class.java, "Pokemon.db").build()
    }
}