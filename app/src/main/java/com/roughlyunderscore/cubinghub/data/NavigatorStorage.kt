package com.roughlyunderscore.cubinghub.data

import com.roughlyunderscore.cubinghub.ui.util.elements.Action

data class NavigatorStorage(
  val navigateToAccount: Action,
  val navigateToAnalysis: Action,
  val navigateToHome: Action,
  val navigateToLogin: Action,
  val navigateToRegister: Action,
  val navigateToChangePassword: Action,
  val navigateToResetPassword: Action,
  val navigateToDeletion: Action,

  val navigateToAlgorithm: (Int) -> Unit,
)