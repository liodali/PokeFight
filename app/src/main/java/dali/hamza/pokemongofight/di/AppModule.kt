package dali.hamza.pokemongofight.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dali.hamza.core.datasource.network.AuthorizationApi
import dali.hamza.core.datasource.network.ClientApi
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

//    @Provides
//    @Named("EmailUser")
//    fun provideEmailUser(application: Application) = application.getString(R.string.tokenEmail)

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
    @Singleton
    @Provides
    fun provideClientApi(retrofit: Retrofit): ClientApi = retrofit.create(ClientApi::class.java)

    @Singleton
    @Provides
    @Named("AuthorizationApi")
    fun provideAuthorizationApi(retrofit: Retrofit): AuthorizationApi = retrofit.create(AuthorizationApi::class.java)

}