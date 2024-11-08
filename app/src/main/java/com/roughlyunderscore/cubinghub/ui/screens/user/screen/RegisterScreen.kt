package com.roughlyunderscore.cubinghub.ui.screens.user.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.roughlyunderscore.cubinghub.R
import com.roughlyunderscore.cubinghub.api.APIRepositoryImpl.Codes.BAD_REQUEST
import com.roughlyunderscore.cubinghub.data.NavigatorStorage
import com.roughlyunderscore.cubinghub.data.UIText
import com.roughlyunderscore.cubinghub.ui.model.ModelStorage
import com.roughlyunderscore.cubinghub.ui.screens.user.base.AccountScreenBase
import com.roughlyunderscore.cubinghub.api.APIRepositoryImpl.Codes.CONFLICT
import com.roughlyunderscore.cubinghub.api.APIRepositoryImpl.Codes.EMPTY_FIELDS
import com.roughlyunderscore.cubinghub.api.APIRepositoryImpl.Codes.NOT_CHECKED_CONFIRMATION
import com.roughlyunderscore.cubinghub.api.APIRepositoryImpl.Codes.NOT_FOUND
import com.roughlyunderscore.cubinghub.api.APIRepositoryImpl.Codes.NO_CONNECTION
import com.roughlyunderscore.cubinghub.api.APIRepositoryImpl.Codes.PASSWORDS_DONT_MATCH
import com.roughlyunderscore.cubinghub.api.APIRepositoryImpl.Codes.SUCCESS
import com.roughlyunderscore.cubinghub.ui.util.elements.ActionButton
import com.roughlyunderscore.cubinghub.ui.util.elements.Checkbox
import com.roughlyunderscore.cubinghub.ui.util.elements.InputField
import com.roughlyunderscore.cubinghub.ui.util.elements.LargeTryAgainPopup
import com.roughlyunderscore.cubinghub.ui.util.elements.SuccessfulRegistrationPopup
import com.roughlyunderscore.cubinghub.ui.util.elements.TryAgainPopup

@Composable
fun RegisterScreen(windowSize: WindowSizeClass, modelStorage: ModelStorage, nav: NavigatorStorage) {
  when (windowSize.widthSizeClass) {
    WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> RegisterScreenCompact(modelStorage, nav)
    WindowWidthSizeClass.Expanded -> RegisterScreenCompact(modelStorage, nav) // TODO: tablet layout (v1.1)
  }
}

@Composable
fun RegisterScreenCompact(modelStorage: ModelStorage, nav: NavigatorStorage) {
  val viewModel = modelStorage.appViewModel
  val registerViewModel = modelStorage.registerViewModel

  val codeState by registerViewModel.resultCode.collectAsState()

  AccountScreenBase(
    viewModel = viewModel,
    nav = nav,
    title = UIText.StringResource(R.string.register).get()
  ) {
    val toResetCodeResult = { registerViewModel.resetCodeResult() }
    // Popups
    if (codeState == NOT_FOUND || codeState == EMPTY_FIELDS || codeState == CONFLICT) {
      TryAgainPopup(
        text = (
          if (codeState == NOT_FOUND || codeState == EMPTY_FIELDS) UIText.StringResource(R.string.creds_not_specified).get()
          else UIText.StringResource(R.string.account_already_exists).get()
        ),
        viewModel = viewModel,
        onDeny = toResetCodeResult,
        onDismiss = toResetCodeResult
      )
    }

    if (codeState == BAD_REQUEST) {
      LargeTryAgainPopup(
        text = UIText.StringResource(R.string.invalid_email_or_unsafe_password).get(),
        viewModel = viewModel,
        onDeny = toResetCodeResult,
        onDismiss = toResetCodeResult
      )
    }

    if (codeState == NO_CONNECTION) {
      TryAgainPopup(
        text = UIText.StringResource(R.string.no_connection).get(),
        viewModel = viewModel,
        onDeny = toResetCodeResult,
        onDismiss = toResetCodeResult
      )
    }

    if (codeState == SUCCESS) {
      SuccessfulRegistrationPopup(
        viewModel = viewModel,
        onDeny = {
          toResetCodeResult()
          nav.navigateToLogin()
        },

        onDismiss = {
          toResetCodeResult()
          nav.navigateToLogin()
        }
      )
    }

    if (codeState == NOT_CHECKED_CONFIRMATION) {
      TryAgainPopup(
        text = UIText.StringResource(R.string.please_confirm).get(),
        viewModel = viewModel,
        onDeny = toResetCodeResult,
        onDismiss = toResetCodeResult
      )
    }

    if (codeState == PASSWORDS_DONT_MATCH) {
      TryAgainPopup(
        text = UIText.StringResource(R.string.passwords_mismatch).get(),
        viewModel = viewModel,
        onDeny = toResetCodeResult,
        onDismiss = toResetCodeResult
      )
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
        onValueChange = { email -> registerViewModel.updateEmailField(email) },
        kbdOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
      )

      InputField(
        viewModel = viewModel,
        placeholder = UIText.StringResource(R.string.password).get(),
        onValueChange = { password -> registerViewModel.updatePasswordField(password) },
        kbdOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        kbdTransformation = PasswordVisualTransformation()
      )

      InputField(
        viewModel = viewModel,
        placeholder = UIText.StringResource(R.string.repeat_password).get(),
        onValueChange = { password -> registerViewModel.updateRepeatPasswordField(password) },
        kbdOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        kbdTransformation = PasswordVisualTransformation()
      )

      // Privacy Policy checkbox
      Checkbox(
        viewModel = viewModel,
        text = UIText.StringResource(R.string.confirm_privacy_policy).get(),
        onCheckedChange = {
          registerViewModel.updateCheckbox()
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
            painter = painterResource(id = R.drawable.outline_person_add_48),
            contentDescription = UIText.StringResource(R.string.register).get()
          )
        },

        label = UIText.StringResource(R.string.register).get(),
        viewModel = viewModel
      ) {
        registerViewModel.attemptRegistration()
      }
    }
  }
}