package com.roughlyunderscore.cubinghub.data.export

import com.roughlyunderscore.cubinghub.data.export.type.Punishment
import com.roughlyunderscore.cubinghub.data.export.type.Solve
import com.roughlyunderscore.cubinghub.util.safeSubList
import com.roughlyunderscore.cubinghub.util.twistyTimeStringToTimestamp

class TwistyTimerAnalyzer : Analyzer {
  override fun fromLine(content: String): Solve? {
    if (content.startsWith("Puzzle,")) return null

    if (content.count { it == ';' } < 4) {
      // session export
      val split = content.replace("\"", "").split(";")

      val initialTime = split.getOrNull(0)?.toDoubleOrNull() ?: return null
      val scramble = split.getOrNull(1) ?: return null
      val timestamp = split.getOrNull(2)?.twistyTimeStringToTimestamp() ?: return null
      val punishment =
        if (split.getOrNull(3) != null) Punishment.DNF
        else Punishment.NONE

      return Solve(initialTime, punishment, timestamp, scramble, "")
    }

    // full export
    // twisty timer for some reason does these in different formats

    val split = content.replace("\"", "").split(";")

    val time = (split.getOrNull(2)?.toDoubleOrNull() ?: return null) / 1000
    val timestamp = split.getOrNull(3)?.toLong() ?: return null
    val scramble = split.getOrNull(4) ?: return null
    val punishment = when (split.getOrNull(5)) {
      "1" -> Punishment.PLUS_TWO
      "2" -> Punishment.DNF
      else -> Punishment.NONE
    }

    // everything else is comment
    val comment = split.safeSubList(6, split.size).joinToString(";")

    return Solve(time, punishment, timestamp, scramble, comment)
  }
}