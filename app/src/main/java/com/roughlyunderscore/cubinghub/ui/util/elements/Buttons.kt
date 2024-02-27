package com.roughlyunderscore.cubinghub.ui.util.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roughlyunderscore.cubinghub.ui.model.AppViewModel

/**
 * A generic action button (not a FAB, but a button with an image and a label).
 * [image] is the image to display; [label] is the label to display; [action] is the action to
 * perform when the button is clicked; [viewModel] is the central view model of the app.
 * [isRed] determines whether to make the text red. [isSmol] determines whether to make
 * the text a size of 10.sp.
 */
@Composable
fun ActionButton(
  image: @Composable Action,
  label: String,
  viewModel: AppViewModel,
  isRed: Boolean = false,
  isSmol: Boolean = false,
  action: Action,
) {
  Column(
    modifier = Modifier
      .clickable(onClick = action)
      .padding(PaddingValues(bottom = 5.dp)),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    image()
    Column(
      modifier = Modifier
        .width(85.dp)
    ) {
      Text(
        text = label,
        style = (
          (if (isRed) getRedButtonTextStyle(font = viewModel.fonts.akatab)
          else getButtonTextStyle(font = viewModel.fonts.akatab)).let {
            if (isSmol) it.copy(fontSize = 10.sp)
            else it
          }
          ),
        modifier = Modifier
          .width(85.dp)
      )
    }
  }
}