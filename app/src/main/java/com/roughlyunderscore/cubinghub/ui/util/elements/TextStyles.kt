package com.roughlyunderscore.cubinghub.ui.util.elements

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.sp
import com.roughlyunderscore.cubinghub.theme.danger_color_400
import com.roughlyunderscore.cubinghub.theme.danger_color_500
import com.roughlyunderscore.cubinghub.theme.info_color_500
import com.roughlyunderscore.cubinghub.theme.neutral_color_500
import com.roughlyunderscore.cubinghub.theme.neutral_color_700
import com.roughlyunderscore.cubinghub.theme.neutral_color_900
import com.roughlyunderscore.cubinghub.theme.success_color_500

fun getSubsetLabelStyle(font: FontFamily) = TextStyle(
  fontSize = 20.sp,
  lineHeight = 27.5.sp,
  fontFamily = font,
  fontWeight = FontWeight.W900,
  color = neutral_color_900,
  textAlign = TextAlign.Center
)

fun getAlgorithmLabelStyle(font: FontFamily) = TextStyle(
  fontSize = 18.sp,
  lineHeight = 18.5.sp,
  fontFamily = font,
  fontWeight = FontWeight.W900,
  color = neutral_color_900,
  textAlign = TextAlign.Center
)


fun getLoggedInStateStyle(font: FontFamily) = TextStyle(
  fontSize = 20.sp,
  lineHeight = 21.5.sp,
  fontFamily = font,
  fontWeight = FontWeight.W400,
  color = neutral_color_900,
  textAlign = TextAlign.Center
)

fun getMailStateStyle(font: FontFamily) = TextStyle(
  fontSize = 20.sp,
  lineHeight = 21.5.sp,
  fontFamily = font,
  fontWeight = FontWeight.W700,
  color = neutral_color_900,
  textAlign = TextAlign.Center
)

fun getPolicyStyle(font: FontFamily) = TextStyle(
  fontSize = 14.sp,
  lineHeight = 15.sp,
  fontFamily = font,
  fontWeight = FontWeight.W400,
  color = neutral_color_900,
  textAlign = TextAlign.Center
)

fun getAlgorithmStyle(font: FontFamily) = TextStyle(
  fontSize = 18.sp,
  lineHeight = 17.sp,
  fontFamily = font,
  fontWeight = FontWeight.W700,
  color = neutral_color_900,
  textAlign = TextAlign.Center
)

fun getCreditUnitStyle(font: FontFamily) = TextStyle(
  fontSize = 10.sp,
  lineHeight = 10.4.sp,
  fontFamily = font,
  fontWeight = FontWeight.W300,
  color = neutral_color_900,
  textAlign = TextAlign.Left,
  textIndent = TextIndent(firstLine = 5.sp)
)

fun getCreditLinkStyle(font: FontFamily) = TextStyle(
  fontSize = 16.sp,
  fontFamily = font,
  fontWeight = FontWeight.W600,
  color = neutral_color_900,
  textDecoration = TextDecoration.Underline,
  textAlign = TextAlign.Center
)

fun getLargeWarningStyle(font: FontFamily) = TextStyle(
  fontSize = 32.sp,
  lineHeight = 39.sp,
  fontFamily = font,
  fontWeight = FontWeight.W900,
  color = danger_color_500,
  textAlign = TextAlign.Center
)

fun getLargeSuccessStyle(font: FontFamily) = TextStyle(
  fontSize = 32.sp,
  lineHeight = 39.sp,
  fontFamily = font,
  fontWeight = FontWeight.W900,
  color = success_color_500,
  textAlign = TextAlign.Center
)

fun getMediumSuccessStyle(font: FontFamily) = TextStyle(
  fontSize = 26.sp,
  lineHeight = 39.sp,
  fontFamily = font,
  fontWeight = FontWeight.W900,
  color = success_color_500,
  textAlign = TextAlign.Center
)

fun getInformationalStyle(font: FontFamily) = TextStyle(
  fontSize = 14.sp,
  lineHeight = 15.sp,
  fontFamily = font,
  fontWeight = FontWeight.W800,
  color = info_color_500,
  textAlign = TextAlign.Center
)

fun getCheckboxStyle(font: FontFamily) = TextStyle(
  fontSize = 10.sp,
  lineHeight = 10.5.sp,
  fontFamily = font,
  fontWeight = FontWeight.W400,
  color = neutral_color_900,
  textAlign = TextAlign.Center
)

fun getMediumCheckboxStyle(font: FontFamily) = TextStyle(
  fontSize = 16.sp,
  lineHeight = 16.sp,
  fontFamily = font,
  fontWeight = FontWeight.W400,
  color = neutral_color_900,
  textAlign = TextAlign.Center
)

fun getFailureStyle(font: FontFamily) = TextStyle(
  fontSize = 14.sp,
  lineHeight = 15.sp,
  fontFamily = font,
  fontWeight = FontWeight.W800,
  color = danger_color_400,
  textAlign = TextAlign.Center
)

fun getButtonTextStyle(font: FontFamily) = TextStyle(
  fontSize = 13.5.sp,
  fontFamily = font,
  fontWeight = FontWeight.W400,
  color = neutral_color_900,
  textAlign = TextAlign.Center,
)

fun getRedButtonTextStyle(font: FontFamily) = TextStyle(
  fontSize = 13.5.sp,
  fontFamily = font,
  fontWeight = FontWeight.W400,
  color = danger_color_500,
  textAlign = TextAlign.Center,
)

fun getInputTextStyle(font: FontFamily) = TextStyle(
  fontSize = 13.sp,
  fontFamily = font,
  fontWeight = FontWeight.W400,
  color = neutral_color_900,
)

fun getRegisterPromptStyle(font: FontFamily) = TextStyle(
  fontSize = 13.sp,
  fontFamily = font,
  fontWeight = FontWeight.W400,
  color = neutral_color_900,
  textDecoration = TextDecoration.Underline
)

fun getWindowTitleStyle(font: FontFamily) = TextStyle(
  fontSize = 16.sp,
  fontFamily = font,
  fontWeight = FontWeight.W900,
  color = neutral_color_900,
  textAlign = TextAlign.Center,
)

fun getPleaseWaitStyle(font: FontFamily) = TextStyle(
  fontSize = 19.sp,
  fontFamily = font,
  lineHeight = 19.2.sp,
  fontWeight = FontWeight.W700,
  color = neutral_color_700,
  textAlign = TextAlign.Center
)

fun getWhenAlgsStyle(font: FontFamily) = TextStyle(
  fontSize = 13.sp,
  fontFamily = font,
  lineHeight = 12.8.sp,
  fontWeight = FontWeight.W600,
  color = neutral_color_500,
  textAlign = TextAlign.Center
)

fun getPleaseSelectFileStyle(font: FontFamily) = TextStyle(
  fontSize = 16.sp,
  fontFamily = font,
  fontWeight = FontWeight.W900,
  color = neutral_color_900,
  textAlign = TextAlign.Center
)

fun getAnalyticStyle(font: FontFamily) = TextStyle(
  fontSize = 16.sp,
  fontFamily = font,
  fontWeight = FontWeight.W900,
  color = info_color_500,
  textAlign = TextAlign.Center
)