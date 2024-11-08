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
import com.roughlyunderscore.cubinghub.data.NavigatorStorage
import com.roughlyunderscore.cubinghub.data.UIText
import com.roughlyunderscore.cubinghub.ui.model.ModelStorage
import com.roughlyunderscore.cubinghub.ui.screens.user.base.AccountScreenBase
import com.roughlyunderscore.cubinghub.api.APIRepositoryImpl.Codes.INTERNAL_SERVER_ERROR
import com.roughlyunderscore.cubinghub.api.APIRepositoryImpl.Codes.NOT_FOUND
import com.roughlyunderscore.cubinghub.api.APIRepositoryImpl.Codes.NO_CONNECTION
import com.roughlyunderscore.cubinghub.api.APIRepositoryImpl.Codes.SUCCESS
import com.roughlyunderscore.cubinghub.api.APIRepositoryImpl.Codes.UNAUTHORIZED
import com.roughlyunderscore.cubinghub.ui.util.elements.ActionButton
import com.roughlyunderscore.cubinghub.ui.util.elements.InputField
import com.roughlyunderscore.cubinghub.ui.util.elements.SuccessfulDeletionPopup
import com.roughlyunderscore.cubinghub.ui.util.elements.TryAgainPopup

@Composable
fun DeletionScreen(windowSize: WindowSizeClass, modelStorage: ModelStorage, nav: NavigatorStorage) {
  when (windowSize.widthSizeClass) {
    WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> DeletionScreenCompact(modelStorage, nav)
    WindowWidthSizeClass.Expanded -> DeletionScreenCompact(modelStorage, nav) // TODO: tablet layout (v1.1)
  }
}

@Composable
fun DeletionScreenCompact(modelStorage: ModelStorage, nav: NavigatorStorage) {
  val viewModel = modelStorage.appViewModel
  val accountViewModel = modelStorage.accountViewModel
  val deletionViewModel = modelStorage.deletionViewModel

  AccountScreenBase(
    viewModel = viewModel,
    nav = nav,
    title = UIText.StringResource(R.string.delete_account).get()
  ) {
    val toResetCodeResult = { deletionViewModel.resetCodeResult() }
    val codeState by deletionViewModel.code.collectAsState()
    val tokenState by accountViewModel.token.collectAsState()

    // Popups
    if (codeState == INTERNAL_SERVER_ERROR) {
      TryAgainPopup(
        text = UIText.StringResource(R.string.server_error).get(),
        viewModel = viewModel,
        onDeny = toResetCodeResult,
        onDismiss = toResetCodeResult
      )
    }

    if (codeState == NOT_FOUND) {
      TryAgainPopup(
        text = UIText.StringResource(R.string.creds_not_specified).get(),
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

    if (codeState == NO_CONNECTION) {
      TryAgainPopup(
        text = UIText.StringResource(R.string.no_connection).get(),
        viewModel = viewModel,
        onDeny = toResetCodeResult,
        onDismiss = toResetCodeResult
      )
    }

    if (codeState == SUCCESS) {
      SuccessfulDeletionPopup(
        viewModel = viewModel,
        onDeny = {
          toResetCodeResult()
          accountViewModel.logout()
          nav.navigateToHome()
        },

        onDismiss = {
          toResetCodeResult()
          accountViewModel.logout()
          nav.navigateToHome()
        }
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
        placeholder = UIText.StringResource(R.string.password).get(),
        onValueChange = { password -> deletionViewModel.updatePasswordField(password) },
        kbdOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        kbdTransformation = PasswordVisualTransformation()
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
            contentDescription = UIText.StringResource(R.string.delete_my_account).get()
          )
        },

        label = UIText.StringResource(R.string.delete_my_account).get(),
        viewModel = viewModel,
        isRed = true
      ) {
        deletionViewModel.deleteAccount(tokenState ?: "")
      }
    }
  }
}