package com.roughlyunderscore.cubinghub.ui.model

import com.roughlyunderscore.cubinghub.ui.screens.algorithm.model.AlgorithmViewModel
import com.roughlyunderscore.cubinghub.ui.screens.analysis.model.AnalysisViewModel
import com.roughlyunderscore.cubinghub.ui.screens.main.model.MainScreenViewModel
import com.roughlyunderscore.cubinghub.ui.screens.user.model.AccountViewModel
import com.roughlyunderscore.cubinghub.ui.screens.user.model.ChangePasswordViewModel
import com.roughlyunderscore.cubinghub.ui.screens.user.model.DeletionViewModel
import com.roughlyunderscore.cubinghub.ui.screens.user.model.LoginViewModel
import com.roughlyunderscore.cubinghub.ui.screens.user.model.RegisterViewModel

data class ModelStorage(
  val appViewModel: AppViewModel,
  val mainScreenViewModel: MainScreenViewModel,
  val accountViewModel: AccountViewModel,
  val loginViewModel: LoginViewModel,
  val registerViewModel: RegisterViewModel,
  val changePasswordViewModel: ChangePasswordViewModel,
  val deletionViewModel: DeletionViewModel,
  val algorithmViewModel: AlgorithmViewModel,
  val analysisViewModel: AnalysisViewModel
)