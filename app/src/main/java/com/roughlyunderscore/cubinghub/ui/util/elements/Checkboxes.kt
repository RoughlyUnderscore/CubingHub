package com.roughlyunderscore.cubinghub.ui.util.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.roughlyunderscore.cubinghub.R
import com.roughlyunderscore.cubinghub.data.UIText
import com.roughlyunderscore.cubinghub.ui.model.AppViewModel

/**
 * A checkbox. [viewModel] is the central view model of the app; [initialValue] is the initial
 * value of the checkbox; [text] is the text to display; [onCheckedChange] is the action to
 * perform when the value of the checkbox changes.
 */
@Composable
fun Checkbox(
  viewModel: AppViewModel,
  initialValue: Boolean = false,
  text: String,
  onCheckedChange: (Boolean) -> Unit,
  style: TextStyle = getCheckboxStyle(font = viewModel.fonts.akatab),
  imageModifier: Modifier = Modifier
) {
  var checkedState by remember { mutableStateOf(initialValue) }
  Row(
    modifier = Modifier
      .clickable(onClick = {
        checkedState = !checkedState
        onCheckedChange(checkedState)
      })
      .padding(PaddingValues(horizontal = 5.dp, vertical = 5.dp)),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Image(
      painter = painterResource(id =
      if (!checkedState) R.drawable.baseline_crop_square_32
      else R.drawable.baseline_check_box_32
      ),
      contentDescription = UIText.DynamicString(text).get(),
      modifier = Modifier
        .padding(PaddingValues(end = 5.dp))
        .then(imageModifier)
    )

    Text(
      text = text,
      style = style,
    )
  }
}

@Composable
fun MediumCheckbox(viewModel: AppViewModel, initialValue: Boolean = false, text: String, onCheckedChange: (Boolean) -> Unit) {
  Checkbox(
    viewModel = viewModel,
    initialValue = initialValue,
    text = text,
    onCheckedChange = onCheckedChange,
    style = getMediumCheckboxStyle(font = viewModel.fonts.akatab),
    imageModifier = Modifier
      .width(48.dp)
      .height(48.dp)
  )
}