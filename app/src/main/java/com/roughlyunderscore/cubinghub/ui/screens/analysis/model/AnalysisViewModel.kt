package com.roughlyunderscore.cubinghub.ui.screens.analysis.model

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.roughlyunderscore.cubinghub.data.export.Analyzer
import com.roughlyunderscore.cubinghub.data.export.type.AnalysisResult
import com.roughlyunderscore.cubinghub.data.export.type.Solve
import com.roughlyunderscore.cubinghub.ui.util.elements.Action
import com.roughlyunderscore.cubinghub.util.ContextHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.apache.commons.io.IOUtils
import javax.inject.Inject

@HiltViewModel
class AnalysisViewModel @Inject constructor(
  private val ctxHandler: ContextHandler
) : ViewModel() {
  private val _currentContent = MutableStateFlow<String?>(null)
  private val _parsedSolves = MutableStateFlow(mutableListOf<Solve>())

  private val _analysis = MutableStateFlow<AnalysisResult?>(null)
  val analysis: StateFlow<AnalysisResult?> = _analysis.asStateFlow()

  fun selectUri(uri: Uri?, commit: Action) {
    _currentContent.update { IOUtils.toString(ctxHandler.resolveContent(uri), Charsets.UTF_8) }
    commit.invoke()
  }

  fun parse() {
    _analysis.update { null } // For visual reset

    val content = _currentContent.value ?: return

    val analyzer = Analyzer.create(content.byteInputStream())

    val lines = mutableListOf<String>()
    content.lines().forEach {
      if (it.isNotEmpty() && (it.first().isDigit() || it.first() == '"' || it.startsWith("No") || it.startsWith("Pu"))) {
        lines.add(it)
      } else {
        lines[lines.size - 1] += " $it"
      }
    }

    val solves = lines.mapNotNull { analyzer.fromLine(it) }.toMutableList()

    _parsedSolves.update { solves }
    _analysis.update { AnalysisResult(solves) }
  }

  fun removeParsed() {
    _parsedSolves.update { mutableListOf() }
    _analysis.update { null }
  }
}