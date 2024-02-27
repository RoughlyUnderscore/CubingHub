package com.roughlyunderscore.cubinghub

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.roughlyunderscore.cubinghub.data.NavigatorStorage
import com.roughlyunderscore.cubinghub.data.UIText
import com.roughlyunderscore.cubinghub.ui.model.ModelStorage
import com.roughlyunderscore.cubinghub.ui.screens.algorithm.screen.AlgorithmScreen
import com.roughlyunderscore.cubinghub.ui.screens.analysis.screen.AnalysisScreen
import com.roughlyunderscore.cubinghub.ui.screens.error.SomethingWentWrongScreen
import com.roughlyunderscore.cubinghub.ui.screens.main.screen.MainMenuScreen
import com.roughlyunderscore.cubinghub.ui.screens.user.screen.AccountScreen
import com.roughlyunderscore.cubinghub.ui.screens.user.screen.ChangePasswordScreen
import com.roughlyunderscore.cubinghub.ui.screens.user.screen.DeletionScreen
import com.roughlyunderscore.cubinghub.ui.screens.user.screen.LoginScreen
import com.roughlyunderscore.cubinghub.ui.screens.user.screen.RegisterScreen
import com.roughlyunderscore.cubinghub.ui.util.elements.ScreenBase
import com.roughlyunderscore.cubinghub.ui.util.elements.getPleaseWaitStyle
import dagger.hilt.android.AndroidEntryPoint

const val DATA_STORE_NAME = "settings"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      val modelStorage = ModelStorage(
        appViewModel = viewModel(),
        mainScreenViewModel = viewModel(),
        accountViewModel = viewModel(),
        loginViewModel = viewModel(),
        registerViewModel = viewModel(),
        changePasswordViewModel = viewModel(),
        deletionViewModel = viewModel(),
        algorithmViewModel = viewModel(),
        analysisViewModel = viewModel()
      )

      val navController = rememberNavController()
      val windowSize = calculateWindowSizeClass(this)

      val navigatorStorage = NavigatorStorage(
        navigateToAccount = { navController.navigate("account_menu") },
        navigateToHome = { navController.navigate("main_menu") },
        navigateToAnalysis = { navController.navigate("analysis") },
        navigateToLogin = { navController.navigate("login") },
        navigateToRegister = { navController.navigate("register") },
        navigateToChangePassword = { navController.navigate("change_password") },
        navigateToResetPassword = { navController.navigate("reset_password") },
        navigateToDeletion = { navController.navigate("deletion") },
        navigateToAlgorithm = { id -> navController.navigate("alg/$id") },
      )

      NavHost(
        navController = navController,
        startDestination = "main_menu",
      ) {
        composable("main_menu") {
          MainMenuScreen(windowSize = windowSize, modelStorage = modelStorage, nav = navigatorStorage)
        }

        composable("account_menu") {
          AccountScreen(windowSize = windowSize, modelStorage = modelStorage, nav = navigatorStorage)
        }

        composable("login") {
          LoginScreen(windowSize = windowSize, modelStorage = modelStorage, nav = navigatorStorage)
        }

        composable("register") {
          RegisterScreen(windowSize = windowSize, modelStorage = modelStorage, nav = navigatorStorage)
        }

        composable("change_password") {
          ChangePasswordScreen(windowSize = windowSize, modelStorage = modelStorage, nav = navigatorStorage)
        }

        composable("deletion") {
          DeletionScreen(windowSize = windowSize, modelStorage = modelStorage, nav = navigatorStorage)
        }

        composable("analysis") {
          AnalysisScreen(windowSize = windowSize, modelStorage = modelStorage, nav = navigatorStorage)
        }

        composable("alg/{id}", arguments = listOf(navArgument("id") { NavType.StringType })) { backStackEntry ->
          backStackEntry.arguments?.getString("id")?.let {
            AlgorithmScreen(
              windowSize = windowSize,
              modelStorage = modelStorage,
              nav = navigatorStorage,
              id = it.toInt()
            )
          } ?: SomethingWentWrongScreen(windowSize = windowSize, modelStorage = modelStorage, nav = navigatorStorage)
        }
      }
    }
  }
}