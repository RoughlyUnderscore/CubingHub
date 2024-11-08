package com.roughlyunderscore.cubinghub.ui.screens.user.model

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roughlyunderscore.cubinghub.api.APIRepository
import com.roughlyunderscore.cubinghub.data.type.Token
import com.roughlyunderscore.cubinghub.util.ContextHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
  private val repository: APIRepository,
  private val ctxHandler: ContextHandler
) : ViewModel() {

  var emailField by mutableStateOf("")
    private set

  var passwordField by mutableStateOf("")
    private set

  private val _authenticatedToken = MutableStateFlow<Token?>(null)
  val authenticatedToken: StateFlow<Token?> = _authenticatedToken.asStateFlow()

  private val _failCode = MutableStateFlow<Int?>(null)
  val failCode: StateFlow<Int?> = _failCode.asStateFlow()

  fun updateEmailField(email: String) {
    emailField = email
  }

  fun updatePasswordField(password: String) {
    passwordField = password
  }

  fun attemptLogin() {
    viewModelScope.launch {
      val result = repository.auth(emailField.trim(), passwordField)

      if (result.isSuccess()) _authenticatedToken.update { result.getSuccess()!! }
      else _failCode.update { result.getFailure()!! }
    }
  }

  fun resetAuthenticationResult() {
    _authenticatedToken.update { null }
    _failCode.update { null }
  }

  fun startActivity(intent: Intent) = ctxHandler.startActivity(intent)
}