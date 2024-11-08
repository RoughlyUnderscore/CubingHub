package com.roughlyunderscore.cubinghub.ui.screens.user.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roughlyunderscore.cubinghub.api.APIRepository
import com.roughlyunderscore.cubinghub.data.type.Email
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
class DeletionViewModel @Inject constructor(
  private val repository: APIRepository,
  private val authManager: AuthenticationManager
) : ViewModel() {
  private val _code = MutableStateFlow(0)
  val code: StateFlow<Int> = _code.asStateFlow()

  private val _passwordField = MutableStateFlow("")
  val passwordField: StateFlow<String> = _passwordField.asStateFlow()

  init {
    viewModelScope.launch {
    }
  }

  fun resetCodeResult() {
    _code.update { 0 }
  }

  fun updatePasswordField(password: String) {
    _passwordField.update { password }
  }

  fun deleteAccount(token: String) {
    viewModelScope.launch {
      _code.update {
        repository.delete(token, _passwordField.value)
      }
    }
  }
}