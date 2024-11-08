package com.roughlyunderscore.cubinghub.ui.screens.main.model

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roughlyunderscore.cubinghub.api.APIRepository
import com.roughlyunderscore.cubinghub.data.Cacher
import com.roughlyunderscore.cubinghub.data.type.Subset
import com.roughlyunderscore.cubinghub.util.ContextHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
  private val repository: APIRepository,
  private val ctxHandler: ContextHandler
) : ViewModel() {
  private val _subsets = MutableStateFlow(mutableListOf<Subset>())
  val subsets: StateFlow<MutableList<Subset>> = _subsets.asStateFlow()

  fun refreshSubsetAlgorithmValues(subsetId: Int) = viewModelScope.launch {
    val oldSubsetName = subsets.value.find { it.id == subsetId }?.name ?: return@launch
    val loadedAlgorithms = repository.getAlgorithms(oldSubsetName).apply {
      if (isEmpty()) return@launch
    }

    _subsets.update {
      _subsets.value.apply {
        find { it.id == subsetId }?.algorithms = loadedAlgorithms
      }
    }
  }

  fun startActivity(intent: Intent) = ctxHandler.startActivity(intent)

  fun loadAlgorithmsForSubset(subsetId: Int) = viewModelScope.launch {
    // Get the server algorithms for this subset for comparison of images etc.
    val subset = subsets.value.find { it.id == subsetId } ?: return@launch
    val serverLoadedAlgs = (subset.name?.let { repository.getAlgorithms(it) } ?: return@launch).apply { if (isEmpty()) return@launch }

    // Await the result of them loading & update the subset
    val updatedAlgs = Cacher.requestSubsetAlgorithmsLoading(subset, serverLoadedAlgs, ctxHandler.self()).await()
    subset.algorithms = updatedAlgs

    // Update _subsets accordingly
    _subsets.update {
      _subsets.value.apply {
        it.replaceAll {
          if (it.id == subsetId) subset
          else it
        }
      }
    }
  }

  init {
    viewModelScope.launch {
      _subsets.update { repository.getSubsets() }
    }
  }
}