package com.roughlyunderscore.cubinghub.data

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UIText {
  data class DynamicString(val value: String): UIText()
  class StringResource(val id: Int): UIText()

  @Composable
  fun get(): String {
    return when (this) {
      is DynamicString -> value
      is StringResource -> stringResource(id = id)
    }
  }

  fun get(ctx: Context): String {
    return when (this) {
      is DynamicString -> value
      is StringResource -> ctx.getString(id)
    }
  }
}
