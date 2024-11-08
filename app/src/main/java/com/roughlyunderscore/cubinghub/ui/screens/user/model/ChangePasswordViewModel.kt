package com.roughlyunderscore.cubinghub.ui.screens.user.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roughlyunderscore.cubinghub.api.APIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
  private val repository: APIRepository,
) : ViewModel() {

  var oldPasswordField by mutableStateOf("")
    private set

  var newPasswordField by mutableStateOf("")
    private set

  var repeatNewPasswordField by mutableStateOf("")
    private set

  private val _resultCode = MutableStateFlow<Int?>(null)
  val resultCode: StateFlow<Int?> = _resultCode.asStateFlow()

  private val _confirmationCheckbox = MutableStateFlow(false)
  val endSessionsCheckbox: StateFlow<Boolean> = _confirmationCheckbox.asStateFlow()

  fun updateOldPasswordField(oldPassword: String) {
    oldPasswordField = oldPassword
  }

  fun updateNewPasswordField(newPassword: String) {
    newPasswordField = newPassword
  }

  fun updateRepeatPasswordField(newPassword: String) {
    repeatNewPasswordField = newPassword
  }

  fun updateCheckbox() {
    _confirmationCheckbox.update { !it }
  }

  fun attemptPasswordChange(token: String) {
    if (newPasswordField != repeatNewPasswordField) {
      _resultCode.update { -2 }
      return
    }

    if (oldPasswordField.isEmpty() || newPasswordField.isEmpty() || repeatNewPasswordField.isEmpty()) {
      _resultCode.update { -3 }
      return
    }

    viewModelScope.launch {
      val result = repository.changePassword(
        token, oldPasswordField, newPasswordField, endSessionsCheckbox.value
      )

      _resultCode.update { result }
    }
  }

  fun resetCodeResult() {
    _resultCode.update { null }
  }
}