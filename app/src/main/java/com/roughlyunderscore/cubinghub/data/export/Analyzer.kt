package com.roughlyunderscore.cubinghub.data.export

import com.roughlyunderscore.cubinghub.data.export.type.Solve
import java.io.InputStream

interface Analyzer {
  fun fromLine(content: String): Solve?

  companion object Factory {
    fun create(type: String): Analyzer {
      return when (type) {
        "twisty_timer" -> TwistyTimerAnalyzer()
        "cs_timer" -> CsTimerAnalyzer()
        else -> throw IllegalArgumentException("Unknown analyzer type")
      }
    }

    fun create(inputStream: InputStream): Analyzer {
      val firstLine = inputStream.bufferedReader().readLine()
      return (
        if (firstLine.startsWith("No.")) CsTimerAnalyzer()
        else TwistyTimerAnalyzer()
      )
    }
  }
}