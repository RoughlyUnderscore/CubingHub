package com.roughlyunderscore.cubinghub.ui.screens.algorithm.model

import androidx.lifecycle.ViewModel
import com.roughlyunderscore.cubinghub.api.APIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlgorithmViewModel @Inject constructor(
  private val repository: APIRepository,
) : ViewModel() {
  suspend fun likeVariation(variationId: Int, token: String?) = token?.let { repository.like(variationId, it) }
  suspend fun dislikeVariation(variationId: Int, token: String?) = token?.let { repository.dislike(variationId, it) }
  suspend fun removeLike(variationId: Int, token: String?) = token?.let { repository.unlike(variationId, it) }
  suspend fun removeDislike(variationId: Int, token: String?) = token?.let { repository.undislike(variationId, it) }
}