package com.roughlyunderscore.cubinghub.ui.screens.user.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roughlyunderscore.cubinghub.api.APIRepository
import com.roughlyunderscore.cubinghub.data.type.Email
import com.roughlyunderscore.cubinghub.data.type.Identification
import com.roughlyunderscore.cubinghub.data.type.Token
import com.roughlyunderscore.cubinghub.util.AuthenticationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
  private val repository: APIRepository,
  private val authManager: AuthenticationManager
) : ViewModel() {
  private val _identification = MutableStateFlow<Identification?>(null)
  val identification: StateFlow<Identification?> = _identification.asStateFlow()

  private val _token = MutableStateFlow<String?>(null)
  val token: StateFlow<String?> = _token.asStateFlow()

  init {
    viewModelScope.launch {
      authManager.saveTokenToMemory()
      _identification.update {
        repository.verifyToken(authManager.token!!)
      }

      _token.update { authManager.token }
    }
  }

  fun logout() {
    viewModelScope.launch {
      repository.logout(authManager.token!!)
      authManager.logout()

      _identification.update { null }
      _token.update { null }
    }
  }

  fun login(token: Token) {
    viewModelScope.launch {
      if (authManager.authenticate(token)) _identification.update { repository.verifyToken(token.token!!) }
      _token.update { token.token }
    }
  }

  val email: String?
    get() = identification.value?.email

  val id: Int?
    get() = identification.value?.id
}