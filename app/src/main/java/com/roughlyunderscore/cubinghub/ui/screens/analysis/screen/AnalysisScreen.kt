package com.roughlyunderscore.cubinghub.ui.screens.analysis.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roughlyunderscore.cubinghub.R
import com.roughlyunderscore.cubinghub.data.NavigatorStorage
import com.roughlyunderscore.cubinghub.data.UIText
import com.roughlyunderscore.cubinghub.theme.neutral_color_900
import com.roughlyunderscore.cubinghub.ui.model.ModelStorage
import com.roughlyunderscore.cubinghub.ui.util.elements.ScreenBase
import com.roughlyunderscore.cubinghub.ui.util.elements.getPleaseSelectFileStyle

@Composable
fun AnalysisScreen(windowSize: WindowSizeClass, modelStorage: ModelStorage, nav: NavigatorStorage) {
  when (windowSize.widthSizeClass) {
    WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> AnalysisScreenCompact(modelStorage, nav)
    WindowWidthSizeClass.Expanded -> AnalysisScreenCompact(modelStorage, nav) // TODO: tablet layout (v1.1)
  }
}

@Composable
fun AnalysisScreenCompact(modelStorage: ModelStorage, nav: NavigatorStorage) {
  val viewModel = modelStorage.appViewModel
  val analysisViewModel = modelStorage.analysisViewModel

  val analysis by analysisViewModel.analysis.collectAsState()

  val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
    analysisViewModel.selectUri(it) { analysisViewModel.parse() }
  }

  ScreenBase(
    scrollable = false,

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

    rightButton = {
      Image(
        painter = painterResource(id = R.drawable.outline_account_circle_24),
        contentDescription = UIText.StringResource(R.string.acc_content_description).get(),
        modifier = Modifier.clickable {
          nav.navigateToAccount()
        }
      )
    },

    title = UIText.StringResource(R.string.export_analyzer).get()
  ) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End),
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .padding(PaddingValues(horizontal = 8.dp))
        .clickable {
          // analysisViewModel.removeParsed()
          launcher.launch(arrayOf("*/*"))
        }
    ) {
      Image(
        painter = painterResource(id = R.drawable.baseline_insert_drive_file_48),
        contentDescription = UIText.StringResource(R.string.please_select_file).get()
      )

      Text(
        text = (
          if (analysis == null) UIText.StringResource(R.string.please_select_file).get()
          else UIText.StringResource(R.string.review_your_analysis).get()
        ),
        style = getPleaseSelectFileStyle(font = viewModel.fonts.akatab).let {
          if (analysis != null) it.copy(textDecoration = TextDecoration.Underline, fontSize = 14.5.sp)
          else it
        }
      )
    }

    if (analysis != null) {
      Divider(color = neutral_color_900, modifier = Modifier.padding(8.dp))
      analysis?.Visual(viewModel)
    }
  }
}