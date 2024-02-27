package com.roughlyunderscore.cubinghub.data.export

import com.roughlyunderscore.cubinghub.data.export.type.Punishment
import com.roughlyunderscore.cubinghub.data.export.type.Solve
import com.roughlyunderscore.cubinghub.util.csTimeStringToTimestamp

class CsTimerAnalyzer : Analyzer {
  override fun fromLine(line: String): Solve? {
    if (line.startsWith("No.")) return null

    val split = line.split(";")

    val punishment = when (split.getOrNull(1)?.last()) {
      '+' -> Punishment.PLUS_TWO
      'F' -> Punishment.DNF
      else -> Punishment.NONE
    }
    val comment = split.getOrNull(2) ?: ""
    val scramble = split.getOrNull(3) ?: return null
    val date = split.getOrNull(4)?.csTimeStringToTimestamp() ?: return null
    val initialTime = split.getOrNull(5)?.toDoubleOrNull() ?: return null

    return Solve(initialTime, punishment, date, scramble, comment)
  }
}