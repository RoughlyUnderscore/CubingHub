package com.roughlyunderscore.cubinghub.ui.screens.algorithm.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableIntStateOf
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
import com.roughlyunderscore.cubinghub.ui.util.elements.AlgorithmPopup
import com.roughlyunderscore.cubinghub.ui.util.elements.ScreenBase
import com.roughlyunderscore.cubinghub.ui.util.elements.algorithmItemModifier
import com.roughlyunderscore.cubinghub.ui.util.elements.algorithmRowModifier
import com.roughlyunderscore.cubinghub.ui.util.elements.getAlgorithmLabelStyle
import com.roughlyunderscore.cubinghub.ui.util.elements.getPleaseWaitStyle

@Composable
fun AlgorithmScreen(windowSize: WindowSizeClass, modelStorage: ModelStorage, nav: NavigatorStorage, id: Int) {
  when (windowSize.widthSizeClass) {
    WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> AlgorithmScreenCompact(modelStorage, nav, id)
    WindowWidthSizeClass.Expanded -> AlgorithmScreenCompact(modelStorage, nav, id) // TODO: tablet layout (v1.1)
  }
}

@Composable
fun AlgorithmScreenCompact(modelStorage: ModelStorage, nav: NavigatorStorage, id: Int) {
  val viewModel = modelStorage.appViewModel
  val mainScreenViewModel = modelStorage.mainScreenViewModel
  val algorithmViewModel = modelStorage.algorithmViewModel
  val accountViewModel = modelStorage.accountViewModel

  val subsets by mainScreenViewModel.subsets.collectAsState()
  val subset = subsets.find { it.id == id } ?: run {
    Text(
      text = UIText.StringResource(R.string.something_went_wrong).get(),
      modifier = Modifier.fillMaxWidth(),
      style = getPleaseWaitStyle(font = viewModel.fonts.akatab)
    )
    return
  }

  var selectedAlgorithm by remember { mutableIntStateOf(-1) }

  val identity by accountViewModel.identification.collectAsState()
  val token by accountViewModel.token.collectAsState()

  val identityCheck = {
    if (identity == null || identity!!.isUnidentified()) {
      selectedAlgorithm = -1
      nav.navigateToAccount()
      false
    }
    else true
  }

  val reset = {
    modelStorage.mainScreenViewModel.refreshSubsetAlgorithmValues(id)
    selectedAlgorithm = -1
  }

  val chillReset: () -> Unit = {
    modelStorage.mainScreenViewModel.refreshSubsetAlgorithmValues(id)
  }

  // Popups
  if (selectedAlgorithm != -1) {
    val algorithm = subset.algorithms.find { it.id == selectedAlgorithm }
    if (algorithm != null) {
      modelStorage.mainScreenViewModel.refreshSubsetAlgorithmValues(id)

      AlgorithmPopup(
        ctxHandler = viewModel.ctxHandler,
        modelStorage = modelStorage,
        algorithm = algorithm,
        reset = chillReset,
        onDismiss = reset,
        onLike = { variationId ->
          if (!identityCheck()) return@AlgorithmPopup false
          else algorithmViewModel.likeVariation(variationId, token) in 200..299
        },
        onDislike = { variationId ->
          if (!identityCheck()) return@AlgorithmPopup false
          else algorithmViewModel.dislikeVariation(variationId, token) in 200..299
        },
        onUndoLike = { variationId ->
          if (!identityCheck()) return@AlgorithmPopup false
          else algorithmViewModel.removeLike(variationId, token) in 200..299
        },
        onUndoDislike = { variationId ->
          if (!identityCheck()) return@AlgorithmPopup false
          else algorithmViewModel.removeDislike(variationId, token) in 200..299
        }
      )
    }
  }

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

    rightButton = {
      Image(
        painter = painterResource(id = R.drawable.outline_account_circle_24),
        contentDescription = UIText.StringResource(R.string.acc_content_description).get(),
        modifier = Modifier.clickable {
          nav.navigateToAccount()
        }
      )
    },

    intrinsic = false,
    title = subset.name ?: "",
  ) {
    // break algorithms into chunks of 4
    val algorithms = subset.algorithms

    if (algorithms.isEmpty()) {
      Text(
        text = UIText.StringResource(R.string.please_wait_for_algs).get(),
        modifier = Modifier.fillMaxWidth(),
        style = getPleaseWaitStyle(font = viewModel.fonts.akatab)
      )
    }

    // Iterate through chunks of 4 (rows)
    for (chunk in algorithms.chunked(4)) {
      Row(
        modifier = Modifier.algorithmRowModifier(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top
      ) {
        // Iterate through columns of chunks (subset buttons)
        for (alg in chunk) {
          // Container for image & text
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
              .padding(PaddingValues(horizontal = 4.dp))
              .clickable {
                selectedAlgorithm = alg.id!!
              }
          ) {
            // Image box (subset button)
            Column(
              modifier = Modifier.algorithmItemModifier(),
              horizontalAlignment = Alignment.CenterHorizontally,
            ) {
              Cacher.AlgorithmImage(
                ctxHandler = viewModel.ctxHandler,
                desc = subset.name!!,
                algorithm = alg
              )
            }
            // Label (subset name)
            Text(
              text = alg.name!!,
              style = getAlgorithmLabelStyle(viewModel.fonts.akatab),
            )
          }
        }
      }
    }
  }
}