package com.roughlyunderscore.cubinghub.ui.screens.error

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.roughlyunderscore.cubinghub.R
import com.roughlyunderscore.cubinghub.data.NavigatorStorage
import com.roughlyunderscore.cubinghub.data.UIText
import com.roughlyunderscore.cubinghub.ui.model.ModelStorage
import com.roughlyunderscore.cubinghub.ui.util.elements.ScreenBase
import com.roughlyunderscore.cubinghub.ui.util.elements.getPleaseWaitStyle

@Composable
fun SomethingWentWrongScreen(windowSize: WindowSizeClass, modelStorage: ModelStorage, nav: NavigatorStorage) {
  when (windowSize.widthSizeClass) {
    WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> SomethingWentWrongScreenCompact(modelStorage, nav)
    WindowWidthSizeClass.Expanded -> SomethingWentWrongScreenCompact(modelStorage, nav) // TODO: tablet layout (v1.1)
  }
}

@Composable
fun SomethingWentWrongScreenCompact(modelStorage: ModelStorage, nav: NavigatorStorage) {
  val viewModel = modelStorage.appViewModel
  ScreenBase(
    viewModel = viewModel,

    leftButton = {
      Image(
        painter = painterResource(id = R.drawable.outline_home_24),
        contentDescription = UIText.StringResource(R.string.home).get(),
        modifier = Modifier.clickable {
          nav.navigateToHome()
        }
      )
    },

    rightButton = {},

    title = UIText.StringResource(R.string.error).get()
  ) {
    Text(
      text = UIText.StringResource(R.string.something_went_wrong).get(),
      style = getPleaseWaitStyle(font = modelStorage.appViewModel.fonts.akatab)
    )
  }
}