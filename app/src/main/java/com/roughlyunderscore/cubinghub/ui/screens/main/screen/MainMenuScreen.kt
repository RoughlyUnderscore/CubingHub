package com.roughlyunderscore.cubinghub.ui.screens.main.screen

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.roughlyunderscore.cubinghub.data.Cacher
import com.roughlyunderscore.cubinghub.data.NavigatorStorage
import com.roughlyunderscore.cubinghub.data.UIText
import com.roughlyunderscore.cubinghub.ui.model.ModelStorage
import com.roughlyunderscore.cubinghub.ui.util.elements.CreditsPopup
import com.roughlyunderscore.cubinghub.ui.util.elements.ScreenBase
import com.roughlyunderscore.cubinghub.ui.util.elements.getPleaseWaitStyle
import com.roughlyunderscore.cubinghub.ui.util.elements.getSubsetLabelStyle
import com.roughlyunderscore.cubinghub.ui.util.elements.getWhenAlgsStyle
import com.roughlyunderscore.cubinghub.ui.util.elements.subsetItemModifier
import com.roughlyunderscore.cubinghub.ui.util.elements.subsetRowModifier

@Composable
fun MainMenuScreen(windowSize: WindowSizeClass, modelStorage: ModelStorage, nav: NavigatorStorage) {
  when (windowSize.widthSizeClass) {
    WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> MainMenuScreenCompact(modelStorage, nav)
    WindowWidthSizeClass.Expanded -> MainMenuScreenCompact(modelStorage, nav) // TODO: tablet layout (v1.1)
  }
}

@Composable
fun MainMenuScreenCompact(modelStorage: ModelStorage, nav: NavigatorStorage) {
  val viewModel = modelStorage.appViewModel
  val mainScreenViewModel = modelStorage.mainScreenViewModel

  var creditsPopupOpen by remember { mutableStateOf(false) }

  val emailAddress = arrayOf(UIText.StringResource(R.string.support_mail).get())

  val clickOnMailAction = {
    val intent = Intent(Intent.ACTION_SEND).apply {
      putExtra(Intent.EXTRA_EMAIL, emailAddress)
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
      type = "message/rfc822"
    }

    mainScreenViewModel.startActivity(intent)
  }

  // Popups
  if (creditsPopupOpen) {
    CreditsPopup(
      viewModel = viewModel,
      onDeny = { creditsPopupOpen = false },
      onDismiss = { creditsPopupOpen = false }
    )
  }

  ScreenBase(
    viewModel = viewModel,

    leftButton = {
      Image(
        painter = painterResource(id = R.drawable.baseline_query_stats_24),
        contentDescription = UIText.StringResource(R.string.analysis_content_description).get(),
        modifier = Modifier.clickable {
          nav.navigateToAnalysis()
        }
      )
    },

    rightButton = {
      Image(
        painter = painterResource(id = R.drawable.outline_account_circle_24),
        contentDescription = UIText.StringResource(R.string.acc_content_description).get(),
        modifier = Modifier.clickable {
          nav.navigateToAccount()
        }
      )
    },

    title = UIText.StringResource(R.string.app_name).get(),
    intrinsic = false,
    clickableTitle = { creditsPopupOpen = true }
  ) {
    // break subsets into chunks of 3
    val subsets by mainScreenViewModel.subsets.collectAsState()

    if (subsets.isEmpty()) {
      Text(
        text = UIText.StringResource(R.string.please_wait_for_algs).get(),
        modifier = Modifier.fillMaxWidth(),
        style = getPleaseWaitStyle(font = viewModel.fonts.akatab)
      )
    }

    // Iterate through chunks of 3 (rows)
    for (chunk in subsets.chunked(3)) {
      Row(
        modifier = Modifier.subsetRowModifier(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top
      ) {
        // Iterate through columns of chunks (subset buttons)
        for (subset in chunk) {
          // Container for image & text
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
              .padding(PaddingValues(horizontal = 6.dp))
              .clickable {
                subset.id?.let { id ->
                  mainScreenViewModel.loadAlgorithmsForSubset(id)
                  nav.navigateToAlgorithm(id)
                }
              }
          ) {
            // Image box (subset button)
            Column(
              modifier = Modifier.subsetItemModifier(),
              horizontalAlignment = Alignment.CenterHorizontally,
            ) {
              Cacher.SubsetImage(
                ctxHandler = viewModel.ctxHandler,
                desc = subset.name!!,
                subset = subset
              )
            }

            // Label (subset name)
            Text(
              text = subset.name!!,
              style = getSubsetLabelStyle(viewModel.fonts.akatab),
            )
          }
        }
      }
    }

    if (subsets.isNotEmpty()) {
      Text(
        modifier = Modifier
          .padding(PaddingValues(vertical = 30.dp, horizontal = 7.dp))
          .fillMaxWidth()
          .clickable { clickOnMailAction() },
        text = UIText.StringResource(R.string.when_new_subsets).get(),
        style = getWhenAlgsStyle(font = viewModel.fonts.akatab)
      )
    }
  }
}