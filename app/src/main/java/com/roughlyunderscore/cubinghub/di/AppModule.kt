package com.roughlyunderscore.cubinghub.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.roughlyunderscore.cubinghub.DATA_STORE_NAME
import com.roughlyunderscore.cubinghub.api.APIRepository
import com.roughlyunderscore.cubinghub.api.APIRepositoryImpl
import com.roughlyunderscore.cubinghub.api.BackendAPI
import com.roughlyunderscore.cubinghub.store.BitmapRepositoryRegistry
import com.roughlyunderscore.cubinghub.store.PreferencesRepository
import com.roughlyunderscore.cubinghub.theme.Fonts
import com.roughlyunderscore.cubinghub.util.AuthenticationManager
import com.roughlyunderscore.cubinghub.util.ContextHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.EventListener
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  @Singleton
  fun provideApi(): BackendAPI {
    return Retrofit.Builder()
      .baseUrl("https://lapis-poppy3487.vm-host.com")
      .addConverterFactory(GsonConverterFactory.create())
      .client(OkHttpClient().newBuilder()
        .retryOnConnectionFailure(true)
        .connectTimeout(Duration.ZERO)
        .build())
      .build()
      .create(BackendAPI::class.java)
  }

  @Provides
  @Singleton
  fun provideRepository(api: BackendAPI, gson: Gson, @ApplicationContext ctx: Context): APIRepository {
    return APIRepositoryImpl(
      api = api,
      gson = gson,
      ctx = ctx
    )
  }

  @Provides
  @Singleton
  fun provideGson(): Gson {
    return GsonBuilder()
      .setLenient()
      .create()
  }

  @Provides
  @Singleton
  fun provideFonts(@ApplicationContext ctx: Context): Fonts {
    return Fonts(ctx)
  }

  @Provides
  @Singleton
  fun provideContextHandler(@ApplicationContext ctx: Context): ContextHandler {
    return ContextHandler(ctx)
  }

  @Provides
  @Singleton
  fun provideAuthenticationManager(
    @ApplicationContext ctx: Context,
    repository: APIRepository,
    preferencesRepository: PreferencesRepository
  ): AuthenticationManager {
    return AuthenticationManager(
      ctx = ctx,
      repository = repository,
      preferencesRepository = preferencesRepository
    )
  }

  @Provides
  @Singleton
  fun provideDataStore(@ApplicationContext ctx: Context): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(
      corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
      produceFile = { ctx.preferencesDataStoreFile(DATA_STORE_NAME) }
    )
  }

  @Provides
  @Singleton
  fun providePreferencesRepo(dataStore: DataStore<Preferences>, @ApplicationContext ctx: Context): PreferencesRepository {
    return PreferencesRepository(
      dataStore = dataStore,
      ctx = ctx
    )
  }

  @Provides
  @Singleton
  fun provideBitmapRepositoryRegistry(): BitmapRepositoryRegistry {
    return BitmapRepositoryRegistry()
  }

}