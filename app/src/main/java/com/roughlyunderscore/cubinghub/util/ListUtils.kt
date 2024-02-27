package com.roughlyunderscore.cubinghub.util

import com.roughlyunderscore.cubinghub.data.export.type.Punishment
import com.roughlyunderscore.cubinghub.data.export.type.Solve

fun <T> List<T>.safeSubList(from: Int, to: Int) = try {
  subList(from, to)
} catch (e: Exception) {
  emptyList()
}

fun List<Solve>.solveAverage(): Double {
  if (this.count { it.timeWithPenalty.isNaN() } > 1) return Double.NaN
  if (this.size == 3) this.solveMean()

  return this
    .map { if (it.timeWithPenalty.isNaN()) Solve(Double.MAX_VALUE, Punishment.NONE, it.timestamp, it.scramble, it.comment) else it }
    .sortedBy { it.timeWithPenalty }
    .drop(1).dropLast(1)
    .sumOf { it.timeWithPenalty } / (this.size - 2)
}

fun List<Solve>.solveMean(): Double {
  if (this.count { it.timeWithPenalty.isNaN() } > 1) return Double.NaN

  return this.sumOf { it.timeWithPenalty } / this.size
}

fun List<Solve>.atTime(time: IntRange) = this.all { it.atTime(time) }