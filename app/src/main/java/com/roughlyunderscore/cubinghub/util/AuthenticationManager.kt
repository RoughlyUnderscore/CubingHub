package com.roughlyunderscore.cubinghub.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.asLiveData
import com.roughlyunderscore.cubinghub.api.APIRepository
import com.roughlyunderscore.cubinghub.data.type.Token
import com.roughlyunderscore.cubinghub.dataStore
import com.roughlyunderscore.cubinghub.store.PreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthenticationManager @Inject constructor(
  @ApplicationContext private val ctx: Context,
  private val preferencesRepository: PreferencesRepository,
  private val repository: APIRepository,
) {
  var token: String? = null
    private set

  suspend fun saveTokenToMemory() {
    preferencesRepository.tokenFlow.firstOrNull().let { token = (it ?: "") }
  }

  suspend fun authenticate(token: Token): Boolean {
    val tokenString = token.token ?: return false
    if (repository.verifyToken(tokenString) == null) return false

    this.token = tokenString
    preferencesRepository.editToken(tokenString)

    return true
  }

  suspend fun logout() {
    token = null
    preferencesRepository.editToken("")
  }
}