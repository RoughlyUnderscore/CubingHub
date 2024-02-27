package com.roughlyunderscore.cubinghub.ui.util.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roughlyunderscore.cubinghub.theme.brand_color_100
import com.roughlyunderscore.cubinghub.theme.brand_color_200
import com.roughlyunderscore.cubinghub.theme.neutral_color_800
import com.roughlyunderscore.cubinghub.theme.neutral_color_900
import com.roughlyunderscore.cubinghub.ui.model.AppViewModel

/**
 * A base for every screen in the app. A central [viewModel] is required to load fonts;
 * [leftButton] and [rightButton] are used to display buttons on the left and right of the header;
 * [content] is the main content of the screen; [title] is the title of the screen, displayed
 * in the header.
 */
@Composable
fun ScreenBase(
  viewModel: AppViewModel,
  leftButton: @Composable Action,
  rightButton: @Composable Action,
  title: String,
  clickableTitle: Action? = null,
  intrinsic: Boolean = true,
  scrollable: Boolean = true,
  content: @Composable Action,
) {
  // Main container
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(neutral_color_900)
  ) {

    // Content container
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(PaddingValues(horizontal = 20.dp, vertical = 21.dp))
        .border(width = 3.dp, color = neutral_color_800, shape = RoundedCornerShape(size = 10.dp))
        .background(brand_color_100, shape = RoundedCornerShape(size = 10.dp))
    ) {

      // Header
      Row(
        modifier = Modifier
          .padding(PaddingValues(start = 10.dp, end = 10.dp, top = 14.dp, bottom = 28.dp))
          .fillMaxWidth()
          .height(30.dp)
          .background(brand_color_200, shape = RoundedCornerShape(size = 10.dp)),
        verticalAlignment = Alignment.CenterVertically
      ) {

        Box(
          modifier = Modifier.fillMaxWidth()
        ) {
          // Left button
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(PaddingValues(horizontal = 6.dp)),
            contentAlignment = Alignment.CenterStart
          ) {
            leftButton()
          }

          // Title
          Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = title,
              style = getWindowTitleStyle(font = viewModel.fonts.akatab),
              modifier = (
                if (clickableTitle == null) Modifier
                else Modifier.clickable(onClick = clickableTitle)
                )
            )
          }

          // Right button
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(PaddingValues(horizontal = 6.dp)),
            contentAlignment = Alignment.CenterEnd
          ) {
            rightButton()
          }
        }
      }

      // Content
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(PaddingValues(bottom = 4.dp))
          .then(
            if (intrinsic) Modifier.height(IntrinsicSize.Max)
            else Modifier.fillMaxHeight()
          )
          .then(
            if (scrollable) Modifier.verticalScroll(rememberScrollState())
            else Modifier
          )
      ) {
        content()
      }
    }
  }
}