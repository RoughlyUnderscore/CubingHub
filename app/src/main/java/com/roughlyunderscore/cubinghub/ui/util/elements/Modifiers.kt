package com.roughlyunderscore.cubinghub.ui.util.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.NoInspectorInfo
import androidx.compose.ui.unit.dp
import com.roughlyunderscore.cubinghub.theme.brand_color_100
import com.roughlyunderscore.cubinghub.theme.neutral_color_200
import com.roughlyunderscore.cubinghub.theme.neutral_color_300
import com.roughlyunderscore.cubinghub.theme.neutral_color_800

typealias Action = () -> Unit
val roundedCornerShape5 = RoundedCornerShape(size = 5.dp)
val roundedCornerShape10 = RoundedCornerShape(size = 10.dp)

fun Modifier.subsetItemModifier() = then(Modifier
  .shadow(
    elevation = 4.dp,
    spotColor = neutral_color_800,
    ambientColor = neutral_color_800,
    shape = roundedCornerShape5
  )
  .border(
    width = 1.dp,
    color = neutral_color_800,
    shape = roundedCornerShape5
  )
  .width(84.dp)
  .height(84.dp)
  .background(
    color = brand_color_100,
    shape = roundedCornerShape5
  )
)

fun Modifier.algorithmItemModifier() = then(Modifier
  .shadow(
    elevation = 4.dp,
    spotColor = neutral_color_800,
    ambientColor = neutral_color_800,
    shape = roundedCornerShape5
  )
  .border(
    width = 1.dp,
    color = neutral_color_800,
    shape = roundedCornerShape5
  )
  .width(62.dp)
  .height(72.dp)
  .background(
    color = brand_color_100,
    shape = roundedCornerShape5
  )
)

fun Modifier.subsetImageModifier() = then(Modifier
  .width(84.dp)
  .height(84.dp)
  .clip(roundedCornerShape5)
)

fun Modifier.subsetRowModifier() = then(Modifier
  .fillMaxWidth()
  .padding(PaddingValues(bottom = 35.dp, top = 10.dp, start = 10.dp, end = 10.dp))
)

fun Modifier.algorithmRowModifier() = then(Modifier
  .fillMaxWidth()
  .padding(PaddingValues(bottom = 5.5.dp, top = 5.5.dp, start = 7.5.dp, end = 7.5.dp))
)

fun Modifier.largePopupModifier() = then(Modifier
  .width(300.dp)
  .height(280.dp)
  .background(
    color = neutral_color_200,
    shape = roundedCornerShape10
  )
  .border(
    width = 1.dp,
    color = neutral_color_800,
    shape = roundedCornerShape10
  )
)

fun Modifier.popupModifier() = then(Modifier
  .width(280.dp)
  .height(240.dp)
  .background(
    color = neutral_color_200,
    shape = roundedCornerShape10
  )
  .border(
    width = 1.dp,
    color = neutral_color_800,
    shape = roundedCornerShape10
  )
)

fun Modifier.mediumPopupModifier() = then(Modifier
  .width(280.dp)
  .height(160.dp)
  .background(
    color = neutral_color_200,
    shape = roundedCornerShape10
  )
  .border(
    width = 1.dp,
    color = neutral_color_800,
    shape = roundedCornerShape10
  )
)

fun Modifier.smallPopupModifier() = then(Modifier
  .width(280.dp)
  .height(80.dp)
  .background(
    color = neutral_color_200,
    shape = roundedCornerShape10
  )
  .border(
    width = 1.dp,
    color = neutral_color_800,
    shape = roundedCornerShape10
  )
)

fun Modifier.inputFieldModifier() = then(Modifier
  .padding(PaddingValues(start = 10.dp, end = 10.dp, top = 7.dp, bottom = 6.dp))
  .fillMaxWidth()
  .height(30.dp)
  .background(
    color = neutral_color_300,
    shape = roundedCornerShape10
  )
  .border(
    width = 1.dp,
    color = neutral_color_800,
    shape = roundedCornerShape10
  )
)

fun Modifier.squareInputFieldModifier(multiplier: Double) = then(Modifier
  .padding(PaddingValues(start = 10.dp, end = 10.dp, top = 7.dp, bottom = 6.dp))
  .width((30 * multiplier).dp)
  .height((30 * multiplier).dp)
  .background(
    color = neutral_color_300,
    shape = roundedCornerShape10
  )
  .border(
    width = 1.dp,
    color = neutral_color_800,
    shape = roundedCornerShape10
  )
)

fun Modifier.leftCreditsColumnModifier() = composed(
  NoInspectorInfo
) {
  Modifier
    .fillMaxWidth(0.5f)
    .verticalScroll(rememberScrollState())
    .padding(PaddingValues(start = 3.dp, end = 4.dp, bottom = 3.dp, top = 4.dp))
}

fun Modifier.rightCreditsColumnModifier() = composed(
  NoInspectorInfo
) {
  Modifier
    .fillMaxWidth()
    .fillMaxHeight()
    .verticalScroll(rememberScrollState())
    .padding(PaddingValues(start = 3.dp, end = 4.dp, bottom = 3.dp, top = 4.dp))
}

fun Modifier.bento() = then(
  Modifier
    .background(color = neutral_color_300, shape = roundedCornerShape10)
    .border(width = 1.dp, color = neutral_color_800, shape = roundedCornerShape10)
)