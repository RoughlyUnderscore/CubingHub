package com.roughlyunderscore.cubinghub.ui.model

import androidx.lifecycle.ViewModel
import com.roughlyunderscore.cubinghub.store.BitmapRepositoryRegistry
import com.roughlyunderscore.cubinghub.theme.Fonts
import com.roughlyunderscore.cubinghub.util.ContextHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
  val fonts: Fonts,
  val ctxHandler: ContextHandler,
  val bitmapRepositoryRegistry: BitmapRepositoryRegistry,
) : ViewModel()