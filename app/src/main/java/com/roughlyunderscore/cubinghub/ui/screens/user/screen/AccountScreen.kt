package com.roughlyunderscore.cubinghub.ui.screens.user.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.roughlyunderscore.cubinghub.R
import com.roughlyunderscore.cubinghub.data.UIText
import com.roughlyunderscore.cubinghub.data.NavigatorStorage
import com.roughlyunderscore.cubinghub.ui.model.ModelStorage
import com.roughlyunderscore.cubinghub.ui.screens.user.base.AccountScreenBase
import com.roughlyunderscore.cubinghub.ui.util.elements.ActionButton
import com.roughlyunderscore.cubinghub.ui.util.elements.DeleteConfirmationPopup
import com.roughlyunderscore.cubinghub.ui.util.elements.getLoggedInStateStyle
import com.roughlyunderscore.cubinghub.ui.util.elements.getMailStateStyle

@Composable
fun AccountScreen(windowSize: WindowSizeClass, modelStorage: ModelStorage, nav: NavigatorStorage) {
  val accountViewModel = modelStorage.accountViewModel
  val identity by accountViewModel.identification.collectAsState()

  when (windowSize.widthSizeClass) {
    WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> {
      identity?.let {
        if (it.isUnidentified()) AccountScreenUnauthenticatedCompact(modelStorage, nav)
        else AccountScreenAuthenticatedCompact(modelStorage, nav, it.email!!)
      } ?: AccountScreenUnauthenticatedCompact(modelStorage, nav)
    }

    WindowWidthSizeClass.Expanded -> {
      identity?.let {
        if (it.isUnidentified()) AccountScreenUnauthenticatedCompact(modelStorage, nav)
        else AccountScreenAuthenticatedCompact(modelStorage, nav, it.email!!)
      } ?: AccountScreenUnauthenticatedCompact(modelStorage, nav) // TODO: tablet layout (v1.1)
    }
  }
}

@Composable
fun AccountScreenAuthenticatedCompact(modelStorage: ModelStorage, nav: NavigatorStorage, mail: String) {
  val viewModel = modelStorage.appViewModel
  val accountViewModel = modelStorage.accountViewModel

  var deleteAccountConfirmationPopup by remember { mutableStateOf(false) }

  if (deleteAccountConfirmationPopup) {
    DeleteConfirmationPopup(
      viewModel = viewModel,
      onDeny = { deleteAccountConfirmationPopup = false },
      onDismiss = { deleteAccountConfirmationPopup = false },
    ) {
      deleteAccountConfirmationPopup = false
      nav.navigateToDeletion()
    }
  }

  AccountScreenBase(
    viewModel = viewModel,
    nav = nav,
    title = UIText.StringResource(R.string.account_management_title).get()
  ) {
    // Info
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(PaddingValues(top = 15.dp, bottom = 30.dp, start = 16.dp, end = 16.dp)),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
        text = UIText.StringResource(R.string.logged_in_as).get(),
        style = getLoggedInStateStyle(font = viewModel.fonts.akatab),
      )

      Text(
        text = mail,
        style = getMailStateStyle(font = viewModel.fonts.akatab),
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
            painter = painterResource(id = R.drawable.baseline_logout_48),
            contentDescription = UIText.StringResource(R.string.logout).get()
          )
        },

        label = UIText.StringResource(R.string.logout).get(),
        viewModel = viewModel
      ) {
        accountViewModel.logout()
        nav.navigateToHome()
      }

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
        nav.navigateToChangePassword()
      }

      ActionButton(
        image = {
          Image(
            painter = painterResource(id = R.drawable.baseline_delete_forever_48),
            contentDescription = UIText.StringResource(R.string.delete_account).get()
          )
        },

        label = UIText.StringResource(R.string.delete_account).get(),
        viewModel = viewModel
      ) {
        deleteAccountConfirmationPopup = !deleteAccountConfirmationPopup
      }
    }
  }
}

@Composable
fun AccountScreenUnauthenticatedCompact(modelStorage: ModelStorage, nav: NavigatorStorage) {
  val viewModel = modelStorage.appViewModel

  AccountScreenBase(
    viewModel = viewModel,
    nav = nav,
    title = UIText.StringResource(R.string.account_management_title).get()
  ) {
    // Info
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(PaddingValues(top = 15.dp, bottom = 30.dp, start = 16.dp, end = 16.dp)),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
        text = UIText.StringResource(R.string.not_logged_in).get(),
        style = getLoggedInStateStyle(font = viewModel.fonts.akatab),
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
            painter = painterResource(id = R.drawable.baseline_login_48),
            contentDescription = UIText.StringResource(R.string.login).get()
          )
        },

        label = UIText.StringResource(R.string.login).get(),
        viewModel = viewModel
      ) {
        nav.navigateToLogin()
      }

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
        nav.navigateToRegister()
      }
    }
  }
}


