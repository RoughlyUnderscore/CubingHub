package com.roughlyunderscore.cubinghub.ui.screens.user.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roughlyunderscore.cubinghub.api.APIRepository
import com.roughlyunderscore.cubinghub.data.type.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
  private val repository: APIRepository,
) : ViewModel() {

  var emailField by mutableStateOf("")
    private set

  var passwordField by mutableStateOf("")
    private set

  var repeatPasswordField by mutableStateOf("")
    private set

  private val _resultCode = MutableStateFlow<Int?>(null)
  val resultCode: StateFlow<Int?> = _resultCode.asStateFlow()

  private val _confirmationCheckbox = MutableStateFlow(false)
  val confirmationCheckbox: StateFlow<Boolean> = _confirmationCheckbox.asStateFlow()

  fun updateEmailField(email: String) {
    emailField = email
  }

  fun updatePasswordField(password: String) {
    passwordField = password
  }

  fun updateRepeatPasswordField(password: String) {
    repeatPasswordField = password
  }

  fun updateCheckbox() {
    _confirmationCheckbox.update { !it }
  }

  fun attemptRegistration() {
    if (!confirmationCheckbox.value) {
      _resultCode.update { -1 }
      return
    }

    if (passwordField != repeatPasswordField) {
      _resultCode.update { -2 }
      return
    }

    if (emailField.isEmpty() || passwordField.isEmpty() || repeatPasswordField.isEmpty()) {
      _resultCode.update { -3 }
      return
    }

    viewModelScope.launch {
      val result = repository.register(emailField, passwordField)
      _resultCode.update { result }
    }
  }

  fun resetCodeResult() {
    _resultCode.update { null }
  }
}