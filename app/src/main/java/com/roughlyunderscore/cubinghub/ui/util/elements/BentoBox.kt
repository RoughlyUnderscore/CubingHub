package com.roughlyunderscore.cubinghub.ui.util.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roughlyunderscore.cubinghub.theme.neutral_color_300
import com.roughlyunderscore.cubinghub.theme.neutral_color_800
import com.roughlyunderscore.cubinghub.theme.neutral_color_900

@Composable
fun BentoRow(content: @Composable RowScope.() -> Unit) = Row(
  modifier = Modifier
    .fillMaxWidth()
    .padding(PaddingValues(horizontal = 5.dp, vertical = 1.5.dp)),
  horizontalArrangement = Arrangement.spacedBy(5.dp),
  verticalAlignment = Alignment.CenterVertically,
  content = content
)

@Composable
fun NarrowBentoColumn(isLeft: Boolean, content: @Composable ColumnScope.() -> Unit) = Column(
  modifier = Modifier
    .fillMaxWidth(if (isLeft) 0.375f else 1f)
    .padding(PaddingValues(horizontal = 5.dp, vertical = 1.5.dp)),
  verticalArrangement = Arrangement.spacedBy(5.dp),
  horizontalAlignment = Alignment.CenterHorizontally,
  content = content
)

@Composable
fun WideBentoColumn(isLeft: Boolean, content: @Composable ColumnScope.() -> Unit) = Column(
  modifier = Modifier
    .fillMaxWidth(if (isLeft) 0.625f else 1f)
    .padding(PaddingValues(horizontal = 5.dp, vertical = 1.5.dp)),
  verticalArrangement = Arrangement.spacedBy(5.dp),
  horizontalAlignment = Alignment.CenterHorizontally,
  content = content
)

@Composable
fun SmallBentoBox(onClick: Action? = null, scrollable: Boolean = false, content: @Composable ColumnScope.() -> Unit) = Column(
  modifier = Modifier
    .width(125.dp)
    .height(80.dp)
    .background(color = neutral_color_300, shape = roundedCornerShape10)
    .border(width = 1.dp, color = neutral_color_800, shape = roundedCornerShape10)
    .padding(5.dp)
    .then(if (onClick != null) Modifier.clickable { onClick.invoke() } else Modifier)
    .then(if (scrollable) Modifier.verticalScroll(rememberScrollState()) else Modifier),
  verticalArrangement = Arrangement.Center,
  horizontalAlignment = Alignment.CenterHorizontally,
  content = content
)

@Composable
fun LongBentoBox(onClick: Action? = null, scrollable: Boolean = false, content: @Composable ColumnScope.() -> Unit) = Column(
  modifier = Modifier
    .width(125.dp)
    .height(165.dp)
    .background(color = neutral_color_300, shape = roundedCornerShape10)
    .border(width = 1.dp, color = neutral_color_800, shape = roundedCornerShape10)
    .padding(5.dp)
    .then(if (onClick != null) Modifier.clickable { onClick.invoke() } else Modifier)
    .then(if (scrollable) Modifier.verticalScroll(rememberScrollState()) else Modifier),
  verticalArrangement = Arrangement.Center,
  horizontalAlignment = Alignment.CenterHorizontally,
  content = content
)

@Composable
private fun LargeBentoBox(onClick: Action? = null, scrollable: Boolean = false, content: @Composable ColumnScope.() -> Unit) = Column(
  modifier = Modifier
    .width(215.dp)
    .height(165.dp)
    .background(color = neutral_color_300, shape = roundedCornerShape10)
    .border(width = 1.dp, color = neutral_color_800, shape = roundedCornerShape10)
    .padding(5.dp)
    .then(if (onClick != null) Modifier.clickable { onClick.invoke() } else Modifier)
    .then(if (scrollable) Modifier.verticalScroll(rememberScrollState()) else Modifier),
  verticalArrangement = Arrangement.Center,
  horizontalAlignment = Alignment.CenterHorizontally,
  content = content
)

@Composable
fun LargeTableBentoBox(
  contentLeft: @Composable ColumnScope.() -> Unit,
  contentRight: @Composable ColumnScope.() -> Unit,
  title: String,
  font: FontFamily
) = LargeBentoBox {
  // Title with border bottom
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .fillMaxHeight(0.225f),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = title,
      style = getPleaseSelectFileStyle(font).copy(fontSize = 14.sp)
    )
  }

  Divider(color = neutral_color_900)

  // Two columns in a row
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .fillMaxHeight(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Labels column
    Column(
      modifier = Modifier
        .fillMaxWidth(0.5f)
        .fillMaxHeight(),
      verticalArrangement = Arrangement.SpaceBetween,
      horizontalAlignment = Alignment.CenterHorizontally,
      content = contentLeft
    )

    Box(
      modifier = Modifier
        .padding(3.dp)
        .fillMaxHeight()
        .width(1.dp)
        .background(color = neutral_color_900)
    )

    // Data column
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
      verticalArrangement = Arrangement.SpaceBetween,
      horizontalAlignment = Alignment.CenterHorizontally,
      content = contentRight
    )
  }
}
