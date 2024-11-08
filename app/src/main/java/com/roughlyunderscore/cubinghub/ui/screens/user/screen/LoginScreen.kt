package com.roughlyunderscore.cubinghub.ui.screens.user.screen

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.roughlyunderscore.cubinghub.R
import com.roughlyunderscore.cubinghub.data.NavigatorStorage
import com.roughlyunderscore.cubinghub.data.UIText
import com.roughlyunderscore.cubinghub.ui.model.ModelStorage
import com.roughlyunderscore.cubinghub.ui.screens.user.base.AccountScreenBase
import com.roughlyunderscore.cubinghub.ui.util.elements.ActionButton
import com.roughlyunderscore.cubinghub.ui.util.elements.AfterEmailPopup
import com.roughlyunderscore.cubinghub.ui.util.elements.AuthenticatedPopup
import com.roughlyunderscore.cubinghub.ui.util.elements.InputField
import com.roughlyunderscore.cubinghub.ui.util.elements.TryAgainPopup
import com.roughlyunderscore.cubinghub.ui.util.elements.getRegisterPromptStyle

@Composable
fun LoginScreen(windowSize: WindowSizeClass, modelStorage: ModelStorage, nav: NavigatorStorage) {
  when (windowSize.widthSizeClass) {
    WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> LoginScreenCompact(modelStorage, nav)
    WindowWidthSizeClass.Expanded -> LoginScreenCompact(modelStorage, nav) // TODO: tablet layout (v1.1)
  }
}

@Composable
fun LoginScreenCompact(modelStorage: ModelStorage, nav: NavigatorStorage) {
  val viewModel = modelStorage.appViewModel
  val accountViewModel = modelStorage.accountViewModel
  val loginViewModel = modelStorage.loginViewModel

  val failCodeState by loginViewModel.failCode.collectAsState()
  val authenticatedTokenState by loginViewModel.authenticatedToken.collectAsState()
  var afterEmailPopupOpen by remember { mutableStateOf(false) }

  val emailAddress = arrayOf(UIText.StringResource(R.string.support_mail).get())
  val restorePasswordSubject = UIText.StringResource(R.string.restore_password_subject).get()
  val restorePasswordText = UIText.StringResource(R.string.restore_password_text).get()

  val clickOnRestorePasswordAction = {
    loginViewModel.resetAuthenticationResult()

    val intent = Intent(Intent.ACTION_SEND).apply {
      putExtra(Intent.EXTRA_EMAIL, emailAddress)
      putExtra(Intent.EXTRA_SUBJECT, restorePasswordSubject)
      putExtra(Intent.EXTRA_TEXT, restorePasswordText)
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
      type = "message/rfc822"
    }

    loginViewModel.startActivity(intent)

    afterEmailPopupOpen = true
  }

  AccountScreenBase(
    viewModel = viewModel,
    nav = nav,
    title = UIText.StringResource(R.string.login).get()
  ) {

    // Popups
    if (afterEmailPopupOpen) {
      AfterEmailPopup(
        viewModel = viewModel,
        onDeny = {
          afterEmailPopupOpen = false
        },

        onDismiss = {
          afterEmailPopupOpen = false
        }
      )
    }

    if (failCodeState in 401..499) {
      TryAgainPopup(
        text = UIText.StringResource(R.string.invalid_creds).get(),
        viewModel = viewModel,
        onDeny = {
          loginViewModel.resetAuthenticationResult()
        },

        onDismiss = {
          loginViewModel.resetAuthenticationResult()
        }
      )
    }

    if (authenticatedTokenState != null) {
      authenticatedTokenState?.let {
        accountViewModel.login(it)

        AuthenticatedPopup(
          viewModel = viewModel,
          onDeny = {
            nav.navigateToHome()
            loginViewModel.resetAuthenticationResult()
          },
          onDismiss = {
            nav.navigateToHome()
            loginViewModel.resetAuthenticationResult()
          },
        )
      }
    }


    // Fields
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(PaddingValues(top = 10.dp, bottom = 30.dp, start = 16.dp, end = 16.dp)),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      InputField(
        viewModel = viewModel,
        placeholder = UIText.StringResource(R.string.email).get(),
        onValueChange = { email -> loginViewModel.updateEmailField(email) },
        kbdOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
      )

      InputField(
        viewModel = viewModel,
        placeholder = UIText.StringResource(R.string.password).get(),
        onValueChange = { password -> loginViewModel.updatePasswordField(password) },
        kbdOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        kbdTransformation = PasswordVisualTransformation()
      )

      // Not registered?
      Text(
        text = UIText.StringResource(R.string.not_registered_yet).get(),
        style = getRegisterPromptStyle(font = viewModel.fonts.akatab),
        modifier = Modifier.clickable {
          nav.navigateToRegister()
        }
      )
    }


    // Buttons
    Row(
      modifier = Modifier
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
      ActionButton(
        image = {
          Image(
            painter = painterResource(id = R.drawable.baseline_lock_reset_48),
            contentDescription = UIText.StringResource(R.string.reset_password).get()
          )
        },

        label = UIText.StringResource(R.string.reset_password).get(),
        viewModel = viewModel,
        action = clickOnRestorePasswordAction
      )

      ActionButton(
        image = {
          Image(
            painter = painterResource(id = R.drawable.baseline_login_48),
            contentDescription = UIText.StringResource(R.string.login).get()
          )
        },

        label = UIText.StringResource(R.string.login).get(),
        viewModel = viewModel
      ) {
        loginViewModel.attemptLogin()
      }
    }
  }
}