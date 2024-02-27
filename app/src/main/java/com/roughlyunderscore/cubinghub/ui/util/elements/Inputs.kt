package com.roughlyunderscore.cubinghub.ui.util.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.roughlyunderscore.cubinghub.ui.model.AppViewModel

/**
 * A field for user input. [viewModel] is the central view model of the app; [placeholder] is the
 * placeholder text; [onValueChange] is the action to perform when the value of the field changes;
 * [kbdOptions] and [kbdTransformation] are the keyboard options and transformation to apply to the
 * field.
 */
@Composable
fun InputField(
  viewModel: AppViewModel,
  placeholder: String,
  onValueChange: (String) -> Unit,
  kbdOptions: KeyboardOptions = KeyboardOptions.Default,
  kbdTransformation: VisualTransformation = VisualTransformation.None,
) {
  var text by remember { mutableStateOf("") }
  BasicTextField(
    value = text,

    onValueChange = {
      text = it
      onValueChange(it)
    },
    singleLine = true,
    textStyle = getInputTextStyle(font = viewModel.fonts.akatab),
    visualTransformation = kbdTransformation,
    keyboardOptions = kbdOptions,
    decorationBox = { innerTextField ->
      Column {
        Text(
          text = placeholder,
          style = getInputTextStyle(font = viewModel.fonts.akatab),
          modifier = Modifier
            .padding(PaddingValues(horizontal = 5.dp))
        )
        Box(
          modifier = Modifier
            .inputFieldModifier()
            .padding(PaddingValues(horizontal = 5.dp)),
          contentAlignment = Alignment.CenterStart,
        ) {
          innerTextField()
        }
      }
    },

    //modifier = Modifier.inputFieldModifier()
  )
}