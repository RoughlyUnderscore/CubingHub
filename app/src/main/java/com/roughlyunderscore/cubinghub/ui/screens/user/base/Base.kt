package com.roughlyunderscore.cubinghub.ui.screens.user.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.roughlyunderscore.cubinghub.R
import com.roughlyunderscore.cubinghub.data.UIText
import com.roughlyunderscore.cubinghub.data.NavigatorStorage
import com.roughlyunderscore.cubinghub.ui.model.AppViewModel
import com.roughlyunderscore.cubinghub.ui.util.elements.Action
import com.roughlyunderscore.cubinghub.ui.util.elements.ExternalLinkPopup
import com.roughlyunderscore.cubinghub.ui.util.elements.ScreenBase
import com.roughlyunderscore.cubinghub.ui.util.elements.getPolicyStyle

@Composable
internal fun AccountScreenBase(
  viewModel: AppViewModel,
  nav: NavigatorStorage,
  title: String,
  content: @Composable Action,
) {
  ScreenBase(
    viewModel = viewModel,

    leftButton = {
      Image(
        painter = painterResource(id = R.drawable.outline_home_24),
        contentDescription = UIText.StringResource(R.string.home).get(),
        modifier = Modifier.clickable {
          nav.navigateToHome()
        }
      )
    },

    rightButton = {
      Image(
        painter = painterResource(id = R.drawable.baseline_query_stats_24),
        contentDescription = UIText.StringResource(R.string.analysis_content_description).get(),
        modifier = Modifier.clickable {
          nav.navigateToAnalysis()
        }
      )
    },

    title = title
  ) {
    var popupPrivacyPolicyOpen by remember { mutableStateOf(false) }
    var popupTermsOfServiceOpen by remember { mutableStateOf(false) }

    Column(
      modifier = Modifier
        .fillMaxSize()
    ) {
      content()

      Spacer(modifier = Modifier.weight(1f))

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(PaddingValues(5.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        // Privacy Policy & Terms of Service links
        Text(
          text = UIText.StringResource(R.string.privacy_policy).get(),
          style = getPolicyStyle(font = viewModel.fonts.akatab),
          modifier = Modifier.clickable { popupPrivacyPolicyOpen = true }
        )

        Text(
          text = UIText.StringResource(R.string.terms_of_service).get(),
          style = getPolicyStyle(font = viewModel.fonts.akatab),
          modifier = Modifier.clickable { popupTermsOfServiceOpen = true }
        )

        // Popups
        if (popupPrivacyPolicyOpen) {
          ExternalLinkPopup(
            link = UIText.StringResource(R.string.privacy_policy_link).get(),
            viewModel = viewModel,
            onDeny = { popupPrivacyPolicyOpen = false },
            onDismiss = { popupPrivacyPolicyOpen = false },
            onAccept = { popupPrivacyPolicyOpen = false }
          )
        }

        if (popupTermsOfServiceOpen) {
          ExternalLinkPopup(
            link = UIText.StringResource(R.string.terms_of_service_link).get(),
            viewModel = viewModel,
            onDeny = { popupTermsOfServiceOpen = false },
            onDismiss = { popupTermsOfServiceOpen = false },
            onAccept = { popupPrivacyPolicyOpen = false }
          )
        }
      }
    }
  }
}