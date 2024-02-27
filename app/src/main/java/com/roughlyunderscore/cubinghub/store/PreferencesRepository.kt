package com.roughlyunderscore.cubinghub.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.roughlyunderscore.cubinghub.store.PreferencesKeys.TOKEN_PREFERENCES_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
  private val dataStore: DataStore<Preferences>,
  @ApplicationContext private val ctx: Context
) {
  val tokenFlow: Flow<String> = dataStore.data.map {
      preferences -> preferences[TOKEN_PREFERENCES_KEY] ?: ""
  }

  suspend fun editToken(token: String) = dataStore.edit { preferences -> preferences[TOKEN_PREFERENCES_KEY] = token }
}

private object PreferencesKeys {
  val TOKEN_PREFERENCES_KEY = stringPreferencesKey("token")
}