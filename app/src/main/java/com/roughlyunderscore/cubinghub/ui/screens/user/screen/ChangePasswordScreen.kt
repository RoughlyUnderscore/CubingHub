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
import com.roughlyunderscore.cubinghub.api.APIRepositoryImpl.Codes.NOT_FOUND
import com.roughlyunderscore.cubinghub.api.APIRepositoryImpl.Codes.EMPTY_FIELDS
import com.roughlyunderscore.cubinghub.api.APIRepositoryImpl.Codes.NO_CONNECTION
import com.roughlyunderscore.cubinghub.api.APIRepositoryImpl.Codes.PASSWORDS_DONT_MATCH
import com.roughlyunderscore.cubinghub.api.APIRepositoryImpl.Codes.SUCCESS
import com.roughlyunderscore.cubinghub.api.APIRepositoryImpl.Codes.UNAUTHORIZED
import com.roughlyunderscore.cubinghub.data.NavigatorStorage
import com.roughlyunderscore.cubinghub.data.UIText
import com.roughlyunderscore.cubinghub.ui.model.ModelStorage
import com.roughlyunderscore.cubinghub.ui.screens.user.base.AccountScreenBase
import com.roughlyunderscore.cubinghub.ui.util.elements.ActionButton
import com.roughlyunderscore.cubinghub.ui.util.elements.InputField
import com.roughlyunderscore.cubinghub.ui.util.elements.MediumCheckbox
import com.roughlyunderscore.cubinghub.ui.util.elements.SuccessfulPasswordChangePopup
import com.roughlyunderscore.cubinghub.ui.util.elements.TryAgainPopup

@Composable
fun ChangePasswordScreen(windowSize: WindowSizeClass, modelStorage: ModelStorage, nav: NavigatorStorage) {
  when (windowSize.widthSizeClass) {
    WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> ChangePasswordScreenCompact(modelStorage, nav)
    WindowWidthSizeClass.Expanded -> ChangePasswordScreenCompact(modelStorage, nav) // TODO: tablet layout (v1.1)
  }
}

@Composable
fun ChangePasswordScreenCompact(modelStorage: ModelStorage, nav: NavigatorStorage) {
  val viewModel = modelStorage.appViewModel
  val changePasswordViewModel = modelStorage.changePasswordViewModel
  val accountViewModel = modelStorage.accountViewModel

  val codeState by changePasswordViewModel.resultCode.collectAsState()
  val tokenState by accountViewModel.token.collectAsState()
  val checkboxState by changePasswordViewModel.endSessionsCheckbox.collectAsState()

  val toResetCodeResult = {
    changePasswordViewModel.resetCodeResult()
  }

  AccountScreenBase(
    viewModel = viewModel,
    nav = nav,
    title = UIText.StringResource(R.string.change_password).get()
  ) {
    // Popups
    if (codeState == NOT_FOUND || codeState == EMPTY_FIELDS) {
      TryAgainPopup(
        text = UIText.StringResource(R.string.creds_not_specified).get(),
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

    if (codeState == UNAUTHORIZED) {
      TryAgainPopup(
        text = UIText.StringResource(R.string.invalid_password).get(),
        viewModel = viewModel,
        onDeny = toResetCodeResult,
        onDismiss = toResetCodeResult
      )
    }

    if (codeState == SUCCESS) {
      if (checkboxState) accountViewModel.logout()

      SuccessfulPasswordChangePopup(
        viewModel = viewModel,
        onDeny = {
          toResetCodeResult()
          nav.navigateToAccount()
        },

        onDismiss = {
          toResetCodeResult()
          nav.navigateToAccount()
        }
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
        placeholder = UIText.StringResource(R.string.old_password).get(),
        onValueChange = { email -> changePasswordViewModel.updateOldPasswordField(email) },
        kbdOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        kbdTransformation = PasswordVisualTransformation()
      )

      InputField(
        viewModel = viewModel,
        placeholder = UIText.StringResource(R.string.new_password).get(),
        onValueChange = { password -> changePasswordViewModel.updateNewPasswordField(password) },
        kbdOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        kbdTransformation = PasswordVisualTransformation()
      )

      InputField(
        viewModel = viewModel,
        placeholder = UIText.StringResource(R.string.repeat_new_password).get(),
        onValueChange = { password -> changePasswordViewModel.updateRepeatPasswordField(password) },
        kbdOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        kbdTransformation = PasswordVisualTransformation()
      )

      // End sessions checkbox & change password button
      Row(
        modifier = Modifier
          .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
      ) {
        MediumCheckbox(
          viewModel = viewModel,
          text = UIText.StringResource(R.string.end_sessions_checkbox).get(),
          onCheckedChange = {
            changePasswordViewModel.updateCheckbox()
          }
        )

        ActionButton(
          image = {
            Image(
              painter = painterResource(id = R.drawable.baseline_lock_48),
              contentDescription = UIText.StringResource(R.string.change_password).get()
            )
          },

          label = UIText.StringResource(R.string.change_password).get(),
          viewModel = viewModel
        ) {
          changePasswordViewModel.attemptPasswordChange(tokenState!!)
        }
      }
    }
  }
}