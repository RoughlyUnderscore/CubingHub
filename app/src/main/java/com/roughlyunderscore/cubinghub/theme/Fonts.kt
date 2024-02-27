package com.roughlyunderscore.cubinghub.theme

import android.content.Context
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

class Fonts(ctx: Context) {
  /**
   * Licensed under OFL 1.1.
   *
   * Font provided by Google Fonts.
   */
  val akatab = ctx.assets.let { assets ->
    FontFamily(listOf(
      Font("fonts/akatab/akatab-black.ttf", assets, FontWeight.Black),
      Font("fonts/akatab/akatab-bold.ttf", assets, FontWeight.Bold),
      Font("fonts/akatab/akatab-extrabold.ttf", assets, FontWeight.ExtraBold),
      Font("fonts/akatab/akatab-medium.ttf", assets, FontWeight.Medium),
      Font("fonts/akatab/akatab-regular.ttf", assets, FontWeight.Normal),
      Font("fonts/akatab/akatab-semibold.ttf", assets, FontWeight.SemiBold),
    ))
  }
}