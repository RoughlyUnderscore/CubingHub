package com.roughlyunderscore.cubinghub.ui.util.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import com.roughlyunderscore.cubinghub.R
import com.roughlyunderscore.cubinghub.data.Cacher.AlgorithmImage
import com.roughlyunderscore.cubinghub.data.UIText
import com.roughlyunderscore.cubinghub.data.type.Algorithm
import com.roughlyunderscore.cubinghub.data.type.Variation
import com.roughlyunderscore.cubinghub.theme.neutral_color_900
import com.roughlyunderscore.cubinghub.ui.model.AppViewModel
import com.roughlyunderscore.cubinghub.ui.model.ModelStorage
import com.roughlyunderscore.cubinghub.ui.screens.user.model.AccountViewModel
import com.roughlyunderscore.cubinghub.util.ContextHandler
import kotlinx.coroutines.launch

/**
 * A popup that displays a message warning the user that they are about to
 * open an external link. [link] is the link to open. [viewModel] is the central
 * view model of the app.
 */
@Composable
fun ExternalLinkPopup(
  link: String,
  viewModel: AppViewModel,
  onDeny: Action,
  onDismiss: Action,
  onAccept: Action,
  offset: IntOffset = IntOffset(0, -1000)
) {
  val uriHandler = LocalUriHandler.current

  Popup(
    offset = offset,
    alignment = Alignment.Center,
    onDismissRequest = onDismiss,
    properties = PopupProperties(
      dismissOnClickOutside = true,
      excludeFromSystemGesture = true
    )
  ) {
    Column(
      modifier = Modifier
        .popupModifier(),
      horizontalAlignment = Alignment.Start,
      verticalArrangement = Arrangement.Center
    ) {
      Image(
        painter = painterResource(id = R.drawable.baseline_close_24),
        contentDescription = UIText.StringResource(R.string.close).get(),
        modifier = Modifier
          .clickable(onClick = onDeny)
          .padding(PaddingValues(top = 3.dp, end = 3.dp))
          .zIndex(1f)
      )

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {

        Text(
          text = UIText.StringResource(R.string.external_website_warning).get(),
          style = getInformationalStyle(font = viewModel.fonts.akatab),
          modifier = Modifier
            .padding(PaddingValues(top = 1.dp))
        )

        Spacer(modifier = Modifier.weight(1f))

        // Buttons
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(horizontal = 10.dp)),
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          ActionButton(
            image = {
              Image(
                painter = painterResource(id = R.drawable.baseline_subdirectory_arrow_left_48),
                contentDescription = UIText.StringResource(R.string.go_back).get()
              )
            },

            label = UIText.StringResource(R.string.go_back).get(),
            action = onDeny,
            viewModel = viewModel
          )

          ActionButton(
            image = {
              Image(
                painter = painterResource(id = R.drawable.baseline_open_in_browser_48),
                contentDescription = UIText.StringResource(R.string.proceed).get()
              )
            },

            label = UIText.StringResource(R.string.proceed).get(),
            viewModel = viewModel
          ) {
            onAccept()
            uriHandler.openUri(link)
          }
        }
      }
    }

  }
}

/**
 * A popup that displays a message informing the user of how the
 * password restoration process happens. [viewModel] is the central
 * view model of the app.
 */
@Composable
fun AfterEmailPopup(
  viewModel: AppViewModel,
  onDeny: Action,
  onDismiss: Action
) {
  Popup(
    alignment = Alignment.Center,
    onDismissRequest = onDismiss,
    properties = PopupProperties(
      dismissOnClickOutside = true,
      excludeFromSystemGesture = true
    )
  ) {
    Column(
      modifier = Modifier
        .mediumPopupModifier(),
      horizontalAlignment = Alignment.Start,
      verticalArrangement = Arrangement.Center
    ) {
      Image(
        painter = painterResource(id = R.drawable.baseline_close_24),
        contentDescription = UIText.StringResource(R.string.close).get(),
        modifier = Modifier
          .clickable(onClick = onDeny)
          .padding(PaddingValues(top = 3.dp, end = 3.dp))
          .zIndex(1f)
      )

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {

        Text(
          text = UIText.StringResource(R.string.restoration_info).get(),
          style = getInformationalStyle(font = viewModel.fonts.akatab),
          modifier = Modifier
            .padding(PaddingValues(top = 1.dp))
        )

        Spacer(modifier = Modifier.weight(1f))
      }
    }

  }
}

/**
 * A popup that displays a message informing the user that something went wrong.
 * [text] is the text to display. [viewModel] is the central view model of the app.
 */
@Composable
fun TryAgainPopup(
  text: String,
  viewModel: AppViewModel,
  onDeny: Action,
  onDismiss: Action,
) {
  Popup(
    alignment = Alignment.Center,
    onDismissRequest = onDismiss,
    properties = PopupProperties(
      dismissOnClickOutside = true,
      excludeFromSystemGesture = true
    )
  ) {
    Column(
      modifier = Modifier
        .mediumPopupModifier(),
      horizontalAlignment = Alignment.Start,
      verticalArrangement = Arrangement.Center
    ) {
      Image(
        painter = painterResource(id = R.drawable.baseline_close_24),
        contentDescription = UIText.StringResource(R.string.close).get(),
        modifier = Modifier
          .clickable(onClick = onDeny)
          .padding(PaddingValues(top = 3.dp, end = 3.dp))
          .zIndex(1f)
      )

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .verticalScroll(rememberScrollState())
      ) {

        Text(
          text = UIText.StringResource(R.string.something_went_wrong).get(),
          style = getLargeWarningStyle(font = viewModel.fonts.akatab),
          modifier = Modifier
            .padding(PaddingValues(top = 1.dp))
        )

        // Text
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(horizontal = 10.dp, vertical = 10.dp)),
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          Text(
            text = text,
            style = getFailureStyle(font = viewModel.fonts.akatab),
            modifier = Modifier
              .padding(PaddingValues(top = 1.dp))
          )
        }
      }

      Spacer(modifier = Modifier.weight(1f))

    }
  }
}

@Composable
fun LargeTryAgainPopup(
  text: String,
  viewModel: AppViewModel,
  onDeny: Action,
  onDismiss: Action,
) {
  Popup(
    alignment = Alignment.Center,
    onDismissRequest = onDismiss,
    properties = PopupProperties(
      dismissOnClickOutside = true,
      excludeFromSystemGesture = true
    )
  ) {
    Column(
      modifier = Modifier
        .largePopupModifier(),
      horizontalAlignment = Alignment.Start,
      verticalArrangement = Arrangement.Center
    ) {
      Image(
        painter = painterResource(id = R.drawable.baseline_close_24),
        contentDescription = UIText.StringResource(R.string.close).get(),
        modifier = Modifier
          .clickable(onClick = onDeny)
          .padding(PaddingValues(top = 3.dp, end = 3.dp))
          .zIndex(1f)
      )

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .verticalScroll(rememberScrollState())
      ) {

        Text(
          text = UIText.StringResource(R.string.something_went_wrong).get(),
          style = getLargeWarningStyle(font = viewModel.fonts.akatab),
          modifier = Modifier
            .padding(PaddingValues(top = 1.dp))
        )

        // Text
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(horizontal = 10.dp, vertical = 10.dp)),
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          Text(
            text = text,
            style = getFailureStyle(font = viewModel.fonts.akatab),
            modifier = Modifier
              .padding(PaddingValues(top = 1.dp))
          )
        }
      }

      Spacer(modifier = Modifier.weight(1f))

    }
  }
}

/**
 * A popup that asks the user to confirm whether they want to delete their account.
 * [viewModel] is the central view model of the app.
 */
@Composable
fun DeleteConfirmationPopup(
  viewModel: AppViewModel,
  onDeny: Action,
  onDismiss: Action,
  onProceed: Action
) {
  DangerPopup(
    title = UIText.StringResource(R.string.are_you_sure).get(),
    text = UIText.StringResource(R.string.deletion_consequences).get(),
    viewModel = viewModel,
    onDeny = onDeny,
    onDismiss = onDismiss,

    leftButton = {
      ActionButton(
        image = {
          Image(
            painter = painterResource(id = R.drawable.baseline_subdirectory_arrow_left_48),
            contentDescription = UIText.StringResource(R.string.go_back).get()
          )
        },
        label = UIText.StringResource(R.string.go_back).get(),
        viewModel = viewModel,
        isSmol = true
      ) {
        onDeny.invoke()
      }
    },

    rightButton = {
      ActionButton(
        image = {
          Image(
            painter = painterResource(id = R.drawable.baseline_delete_forever_48),
            contentDescription = UIText.StringResource(R.string.deletion_consent).get()
          )
        },
        label = UIText.StringResource(R.string.deletion_consent).get(),
        viewModel = viewModel,
        isRed = true,
        isSmol = true
      ) {
        onProceed.invoke()
      }
    }
  )
}

@Composable
private fun DangerPopup(
  title: String,
  text: String,
  viewModel: AppViewModel,
  onDeny: Action,
  onDismiss: Action,
  leftButton: @Composable Action?,
  rightButton: @Composable Action?
) {
  Popup(
    alignment = Alignment.Center,
    onDismissRequest = onDismiss,
    properties = PopupProperties(
      dismissOnClickOutside = true,
      excludeFromSystemGesture = true
    )
  ) {
    Column(
      modifier = Modifier
        .popupModifier(),
      horizontalAlignment = Alignment.Start,
      verticalArrangement = Arrangement.Center
    ) {
      Image(
        painter = painterResource(id = R.drawable.baseline_close_24),
        contentDescription = UIText.StringResource(R.string.close).get(),
        modifier = Modifier
          .clickable(onClick = onDeny)
          .padding(PaddingValues(top = 3.dp, end = 3.dp))
          .zIndex(1f)
      )

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .verticalScroll(rememberScrollState())
      ) {

        Text(
          text = title,
          style = getLargeWarningStyle(font = viewModel.fonts.akatab),
          modifier = Modifier
            .padding(PaddingValues(top = 1.dp))
        )

        // Text
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(horizontal = 10.dp, vertical = 10.dp)),
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          Text(
            text = text,
            style = getFailureStyle(font = viewModel.fonts.akatab),
            modifier = Modifier
              .padding(PaddingValues(top = 1.dp))
          )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Buttons
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(horizontal = 10.dp)),
          horizontalArrangement = Arrangement.spacedBy(35.dp, Alignment.CenterHorizontally)
        ) {
          if (leftButton != null) leftButton()
          if (rightButton != null) rightButton()
        }
      }
    }

  }
}

/**
 * A popup that displays a message informing the user that they have been authenticated.
 * [viewModel] is the central view model of the app.
 */
@Composable
fun AuthenticatedPopup(
  viewModel: AppViewModel,
  onDeny: Action,
  onDismiss: Action,
) {
  SuccessfulPopup(
    viewModel = viewModel,
    onDeny = onDeny,
    onDismiss = onDismiss,
    text = UIText.StringResource(R.string.authenticated).get()
  )
}

@Composable
private fun SuccessfulPopup(
  viewModel: AppViewModel,
  onDeny: Action,
  onDismiss: Action,
  text: String
) {
  Popup(
    alignment = Alignment.Center,
    onDismissRequest = onDismiss,
    properties = PopupProperties(
      dismissOnClickOutside = true,
      excludeFromSystemGesture = true
    )
  ) {
    Column(
      modifier = Modifier
        .mediumPopupModifier(),
      horizontalAlignment = Alignment.Start,
      verticalArrangement = Arrangement.Center,
    ) {
      Image(
        painter = painterResource(id = R.drawable.baseline_close_24),
        contentDescription = UIText.StringResource(R.string.close).get(),
        modifier = Modifier
          .clickable(onClick = onDeny)
          .padding(PaddingValues(top = 3.dp, end = 3.dp))
          .zIndex(1f)
      )

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .verticalScroll(rememberScrollState())
      ) {

        Text(
          text = UIText.StringResource(R.string.success).get(),
          style = getLargeSuccessStyle(font = viewModel.fonts.akatab),
          modifier = Modifier
            .padding(PaddingValues(top = 1.dp))
        )

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(horizontal = 10.dp, vertical = 5.dp)),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.Top
        ) {
          Text(
            text = text,
            style = getInformationalStyle(font = viewModel.fonts.akatab),
            modifier = Modifier
              .padding(PaddingValues(top = 1.dp))
          )
        }
      }

      Spacer(modifier = Modifier.weight(1f))
    }

  }
}

@Composable
fun MediumPopupWithText(
  viewModel: AppViewModel,
  onDeny: Action,
  onDismiss: Action,
  title: String,
  text: String
) {
  Popup(
    alignment = Alignment.Center,
    onDismissRequest = onDismiss,
    properties = PopupProperties(
      dismissOnClickOutside = true,
      excludeFromSystemGesture = true
    )
  ) {
    Column(
      modifier = Modifier
        .mediumPopupModifier(),
      horizontalAlignment = Alignment.Start,
      verticalArrangement = Arrangement.Center,
    ) {
      Image(
        painter = painterResource(id = R.drawable.baseline_close_24),
        contentDescription = UIText.StringResource(R.string.close).get(),
        modifier = Modifier
          .clickable(onClick = onDeny)
          .padding(PaddingValues(top = 3.dp, end = 3.dp))
          .zIndex(1f)
      )

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .verticalScroll(rememberScrollState())
      ) {

        Text(
          text = title,
          style = getMediumSuccessStyle(font = viewModel.fonts.akatab),
          modifier = Modifier
            .padding(PaddingValues(top = 1.dp))
        )

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(horizontal = 10.dp, vertical = 5.dp)),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.Top
        ) {
          Text(
            text = text,
            style = getInformationalStyle(font = viewModel.fonts.akatab),
            modifier = Modifier
              .padding(PaddingValues(top = 1.dp))
          )
        }
      }

      Spacer(modifier = Modifier.weight(1f))
    }

  }
}

@Composable
fun LargePopupWithText(
  viewModel: AppViewModel,
  onDeny: Action,
  onDismiss: Action,
  title: String,
  text: String
) {
  Popup(
    alignment = Alignment.Center,
    onDismissRequest = onDismiss,
    properties = PopupProperties(
      dismissOnClickOutside = true,
      excludeFromSystemGesture = true
    )
  ) {
    Column(
      modifier = Modifier
        .largePopupModifier(),
      horizontalAlignment = Alignment.Start,
      verticalArrangement = Arrangement.Center,
    ) {
      Image(
        painter = painterResource(id = R.drawable.baseline_close_24),
        contentDescription = UIText.StringResource(R.string.close).get(),
        modifier = Modifier
          .clickable(onClick = onDeny)
          .padding(PaddingValues(top = 3.dp, end = 3.dp))
          .zIndex(1f)
      )

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .verticalScroll(rememberScrollState()),
      ) {
        Text(
          text = title,
          style = getMediumSuccessStyle(font = viewModel.fonts.akatab),
          modifier = Modifier
            .padding(PaddingValues(top = 1.dp))
        )

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(horizontal = 10.dp, vertical = 5.dp)),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.Top
        ) {
          Text(
            text = text,
            style = getInformationalStyle(font = viewModel.fonts.akatab),
            modifier = Modifier
              .padding(PaddingValues(top = 1.dp))
          )
        }
      }

      Spacer(modifier = Modifier.weight(1f))
    }

  }
}

@Composable
fun AlgorithmPopup(
  ctxHandler: ContextHandler,
  modelStorage: ModelStorage,
  algorithm: Algorithm,
  onDismiss: Action,
  reset: Action,
  onLike: suspend (Int) -> Boolean,
  onDislike: suspend (Int) -> Boolean,
  onUndoLike: suspend (Int) -> Boolean,
  onUndoDislike: suspend (Int) -> Boolean
) {
  val name = algorithm.exhibit ?: "?"
  val sortedVariations = algorithm.sortedVariations

  val viewModel = modelStorage.appViewModel

  Popup(
    alignment = Alignment.Center,
    onDismissRequest = onDismiss,
    properties = PopupProperties(
      dismissOnClickOutside = true,
      excludeFromSystemGesture = true
    )
  ) {
    // Content
    Row(
      modifier = Modifier
        .popupModifier()
    ) {
      // Left column: exhibit image and name
      Column(
        modifier = Modifier
          .width(60.dp)
          .fillMaxHeight()
          // Only right border
          .border(
            BorderStroke(1.dp, neutral_color_900),
            shape = RoundedCornerShape(10.dp, 0.dp, 0.dp, 10.dp)
          ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Image and name column
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(horizontal = 3.dp)),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          // Image
          AlgorithmImage(
            modifier = Modifier
              .size(54.dp)
              .clip(roundedCornerShape10),
            ctxHandler = ctxHandler,
            desc = name,
            contentScale = ContentScale.Inside,
            algorithm = algorithm
          )

          // Name
          Text(
            text = name,
            style = getWindowTitleStyle(font = viewModel.fonts.akatab),
            modifier = Modifier
              .fillMaxWidth()
              .padding(PaddingValues(horizontal = 3.dp))
          )
        }
      }

      // Right column: algorithm details
      Column(
        modifier = Modifier
          .fillMaxHeight()
          .fillMaxWidth()
          .padding(PaddingValues(horizontal = 5.dp, vertical = 8.dp)),
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Fill as many algorithms as possible...
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .verticalScroll(rememberScrollState()),
          verticalArrangement = Arrangement.Center
        ) {
          val accountViewModel = modelStorage.accountViewModel

          for (variation in sortedVariations) {
            AlgorithmString(
              variation = variation,
              accountViewModel = accountViewModel,
              viewModel = viewModel,
              reset = reset,
              onLike = onLike,
              onDislike = onDislike,
              onUndoLike = onUndoLike,
              onUndoDislike = onUndoDislike
            )
          }
        }
      }

      Spacer(modifier = Modifier.weight(1f))
    }

  }
}

/**
 * A popup that displays a message informing the user that they have successfully
 * registered their account.
 * [viewModel] is the central view model of the app.
 */
@Composable
fun SuccessfulRegistrationPopup(
  viewModel: AppViewModel,
  onDeny: Action,
  onDismiss: Action,
) {
  SuccessfulPopup(
    viewModel = viewModel,
    onDeny = onDeny,
    onDismiss = onDismiss,
    text = UIText.StringResource(R.string.registered).get()
  )
}

/**
 * A popup that displays a message informing the user that they have successfully
 * deleted their account.
 * [viewModel] is the central view model of the app.
 */
@Composable
fun SuccessfulDeletionPopup(
  viewModel: AppViewModel,
  onDeny: Action,
  onDismiss: Action,
) {
  SuccessfulPopup(
    viewModel = viewModel,
    onDeny = onDeny,
    onDismiss = onDismiss,
    text = UIText.StringResource(R.string.deleted).get()
  )
}

/**
 * A popup that displays a message informing the user that they have
 * successfully changed their password.
 * [viewModel] is the central view model of the app.
 */
@Composable
fun SuccessfulPasswordChangePopup(
  viewModel: AppViewModel,
  onDeny: Action,
  onDismiss: Action,
) {
  SuccessfulPopup(
    viewModel = viewModel,
    onDeny = onDeny,
    onDismiss = onDismiss,
    text = UIText.StringResource(R.string.password_changed).get()
  )
}

@Composable
fun CreditsPopup(
  viewModel: AppViewModel,
  onDeny: Action,
  onDismiss: Action
) {
  val akatabAttribution = UIText.StringResource(R.string.akatab_credit).get()
  val akatabLink = UIText.StringResource(R.string.akatab_link).get()

  val gsonAttribution = UIText.StringResource(R.string.gson_credit).get()
  val gsonLink = UIText.StringResource(R.string.gson_link).get()

  val apacheIoAttribution = UIText.StringResource(R.string.apache_io_credit).get()
  val apacheIoLink = UIText.StringResource(R.string.apache_io_link).get()

  val googleIconsAttribution = UIText.StringResource(R.string.google_icons_credit).get()

  val coilAttribution = UIText.StringResource(R.string.coil_credit).get()
  val coilLink = UIText.StringResource(R.string.coil_link).get()

  val retrofitAttribution = UIText.StringResource(R.string.retrofit_credit).get()
  val retrofitLink = UIText.StringResource(R.string.retrofit_link).get()

  val daggerHiltAttribution = UIText.StringResource(R.string.dagger_hilt_credit).get()
  val daggerHiltLink = UIText.StringResource(R.string.dagger_hilt_link).get()

  val apisAttribution = UIText.StringResource(R.string.apis_credit).get()
  val apisLink = UIText.StringResource(R.string.apis_link).get()


  val termsOfService = UIText.StringResource(R.string.terms_of_service).get()
  val termsOfServiceLink = UIText.StringResource(R.string.terms_of_service_link).get()

  val privacyPolicy = UIText.StringResource(R.string.privacy_policy).get()
  val privacyPolicyLink = UIText.StringResource(R.string.privacy_policy_link).get()

  val sourceCode = UIText.StringResource(R.string.source_code).get()
  val sourceCodeLink = UIText.StringResource(R.string.source_code_link).get()

  val backendSourceCode = UIText.StringResource(R.string.backend_source_code).get()
  val backendSourceCodeLink = UIText.StringResource(R.string.backend_source_code_link).get()


  val uriHandler = LocalUriHandler.current

  Popup(
    alignment = Alignment.Center,
    onDismissRequest = onDismiss,
    properties = PopupProperties(
      dismissOnClickOutside = true,
      excludeFromSystemGesture = true
    )
  ) {
    Column(
      modifier = Modifier.largePopupModifier(),
      horizontalAlignment = Alignment.Start,
      verticalArrangement = Arrangement.Center,
    ) {
      Row(
        modifier = Modifier.fillMaxWidth()
      ) {
        Box(
          contentAlignment = Alignment.TopStart,
        ) {
          Image(
            painter = painterResource(id = R.drawable.baseline_close_24),
            contentDescription = UIText.StringResource(R.string.close).get(),
            modifier = Modifier
              .clickable(onClick = onDeny)
              .padding(PaddingValues(top = 3.dp, end = 3.dp))
              .zIndex(1f)
          )
        }

        Box(
          modifier = Modifier.fillMaxWidth(),
          contentAlignment = Alignment.TopCenter,
        ) {
          Text(
            text = UIText.StringResource(R.string.app_name).get(),
            style = getLargeSuccessStyle(font = viewModel.fonts.akatab).copy(color = neutral_color_900)
          )
        }
      }

      Row(
        modifier = Modifier.fillMaxSize()
      ) {
        Column(
          modifier = Modifier.leftCreditsColumnModifier()
        ) {
          Text(
            text = UIText.StringResource(R.string.credits_introductory).get(),
            style = getPolicyStyle(font = viewModel.fonts.akatab)
          )

          Column(
            modifier = Modifier
              .padding(PaddingValues(horizontal = 4.dp))
          ) {
            CreditUnit(viewModel = viewModel, attribution = akatabAttribution, link = akatabLink, handler = uriHandler)
            CreditUnit(viewModel = viewModel, attribution = gsonAttribution, link = gsonLink, handler = uriHandler)
            CreditUnit(viewModel = viewModel, attribution = apacheIoAttribution, link = apacheIoLink, handler = uriHandler)
            CreditUnit(viewModel = viewModel, attribution = googleIconsAttribution, link = null, handler = uriHandler)
            CreditUnit(viewModel = viewModel, attribution = coilAttribution, link = coilLink, handler = uriHandler)
            CreditUnit(viewModel = viewModel, attribution = retrofitAttribution, link = retrofitLink, handler = uriHandler)
            CreditUnit(viewModel = viewModel, attribution = daggerHiltAttribution, link = daggerHiltLink, handler = uriHandler)
            CreditUnit(viewModel = viewModel, attribution = apisAttribution, link = apisLink, handler = uriHandler)

            CreditInspiration(viewModel = viewModel, handler = uriHandler)
            CreditAreYouSubX(viewModel = viewModel, handler = uriHandler)
            CreditImages(viewModel = viewModel, handler = uriHandler)
            CreditAlgs(viewModel = viewModel, handler = uriHandler)
          }
        }

        Column(
          modifier = Modifier.rightCreditsColumnModifier(),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          LinkUnit(viewModel = viewModel, name = termsOfService, link = termsOfServiceLink, handler = uriHandler)
          LinkUnit(viewModel = viewModel, name = privacyPolicy, link = privacyPolicyLink, handler = uriHandler)
          LinkUnit(viewModel = viewModel, name = sourceCode, link = sourceCodeLink, handler = uriHandler)
          LinkUnit(viewModel = viewModel, name = backendSourceCode, link = backendSourceCodeLink, handler = uriHandler)

          Spacer(modifier = Modifier.weight(1f))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
          ) {
            Text(
              text = UIText.StringResource(R.string.author_year).get(),
              style = getWindowTitleStyle(font = viewModel.fonts.akatab).copy(fontSize = 14.sp)
            )
          }
        }
      }
    }

  }
}

@Composable
private fun CreditUnit(handler: UriHandler, viewModel: AppViewModel, attribution: String, link: String?) = Text(
  text = attribution,
  style = getCreditUnitStyle(font = viewModel.fonts.akatab),
  modifier = Modifier.clickable { link?.let { handler.openUri(it) } }
)

@Composable
private fun CreditInspiration(handler: UriHandler, viewModel: AppViewModel) = Text(
  text = UIText.StringResource(R.string.speedcubedb_inspiration).get(),
  style = getCreditUnitStyle(font = viewModel.fonts.akatab),
  modifier = UIText.StringResource(R.string.speedcubedb_link).get().let { link -> Modifier.clickable { handler.openUri(link) } }
)

@Composable
private fun CreditAlgs(handler: UriHandler, viewModel: AppViewModel) = Text(
  text = UIText.StringResource(R.string.algs_speedcubedb).get(),
  style = getCreditUnitStyle(font = viewModel.fonts.akatab),
  modifier = UIText.StringResource(R.string.speedcubedb_link).get().let { link -> Modifier.clickable { handler.openUri(link) } }
)

@Composable
private fun CreditAreYouSubX(handler: UriHandler, viewModel: AppViewModel) = Text(
  text = UIText.StringResource(R.string.are_you_sub_x_credit).get(),
  style = getCreditUnitStyle(font = viewModel.fonts.akatab),
  modifier = UIText.StringResource(R.string.cubingapp_link).get().let { link -> Modifier.clickable { handler.openUri(link) } }
)

@Composable
private fun CreditImages(handler: UriHandler, viewModel: AppViewModel) = Text(
  text = UIText.StringResource(R.string.algorithm_images).get(),
  style = getCreditUnitStyle(font = viewModel.fonts.akatab),
  modifier = UIText.StringResource(R.string.cubing_net_link).get().let { link -> Modifier.clickable { handler.openUri(link) } }
)

@Composable
private fun LinkUnit(handler: UriHandler, viewModel: AppViewModel, name: String, link: String) = Text(
  text = name,
  style = getCreditLinkStyle(font = viewModel.fonts.akatab),
  modifier = Modifier
    .fillMaxWidth()
    .clickable { handler.openUri(link) }
)

@Composable
private fun AlgorithmString(
  variation: Variation,
  accountViewModel: AccountViewModel,
  viewModel: AppViewModel,
  reset: Action,
  onLike: suspend (Int) -> Boolean,
  onDislike: suspend (Int) -> Boolean,
  onUndoLike: suspend (Int) -> Boolean,
  onUndoDislike: suspend (Int) -> Boolean
) = Row(
  modifier = Modifier
    .fillMaxWidth()
    .padding(PaddingValues(horizontal = 4.dp)),
  verticalAlignment = Alignment.CenterVertically,
) {
  val text = variation.value ?: "?"

  val liked = (variation.liked ?: emptyList()).filter { it.isNotBlank() && it.isNotEmpty() }
  val disliked = (variation.disliked ?: emptyList()).filter { it.isNotBlank() && it.isNotEmpty() }

  var likedSize by remember { mutableIntStateOf(liked.size) }
  var dislikedSize by remember { mutableIntStateOf(disliked.size) }

  val id = accountViewModel.id

  var isLikedByThisUser by remember { mutableStateOf(liked.contains(id?.toString())) }
  var isDislikedByThisUser by remember { mutableStateOf(disliked.contains(id?.toString())) }

  Row(
    modifier = Modifier
      .fillMaxWidth(0.7f)
      .padding(PaddingValues(horizontal = 2.dp)),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = text,
      style = getAlgorithmStyle(font = viewModel.fonts.akatab)
    )
  }

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(PaddingValues(horizontal = 2.dp)),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    val likeResourceId =
      if (isLikedByThisUser) R.drawable.baseline_thumb_up_24
      else R.drawable.outline_thumb_up_24

    val dislikeResourceId =
      if (isDislikedByThisUser) R.drawable.baseline_thumb_down_24
      else R.drawable.outline_thumb_down_24

    val coroutineScope = rememberCoroutineScope()

    val likeAction = { id: Int -> coroutineScope.launch {
      if (!(if (!isLikedByThisUser) onLike(id) else onUndoLike(id))) return@launch

      isLikedByThisUser = !isLikedByThisUser
      likedSize = if (isLikedByThisUser) likedSize + 1 else likedSize - 1

      if (isDislikedByThisUser) {
        isDislikedByThisUser = false
        dislikedSize--
      }
    }}

    val dislikeAction = { id: Int -> coroutineScope.launch {
      if (!(if (!isDislikedByThisUser) onDislike(id) else onUndoDislike(id))) return@launch

      isDislikedByThisUser = !isDislikedByThisUser
      dislikedSize = if (isDislikedByThisUser) dislikedSize + 1 else dislikedSize - 1

      if (isLikedByThisUser) {
        isLikedByThisUser = false
        likedSize--
      }
    }}

    Column(
      verticalArrangement = Arrangement.spacedBy(3.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Image(
        painter = painterResource(id = likeResourceId),
        contentDescription = UIText.StringResource(R.string.like).get(),
        modifier = Modifier.clickable {
          likeAction(variation.id!!)
          reset()
        }
      )

      Text(
        text = likedSize.toString(),
        style = getAlgorithmStyle(font = viewModel.fonts.akatab)
      )
    }

    Column(
      verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
      Image(
        painter = painterResource(id = dislikeResourceId),
        contentDescription = UIText.StringResource(R.string.dislike).get(),
        modifier = Modifier.clickable {
          dislikeAction(variation.id!!)
          reset()
        }
      )

      Text(
        text = dislikedSize.toString(),
        style = getAlgorithmStyle(font = viewModel.fonts.akatab)
      )
    }

  }
}
