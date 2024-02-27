package com.roughlyunderscore.cubinghub.data.export.type

data class Solve(
  val initialTime: Double,
  val punishment: Punishment,
  val timestamp: Long,
  val scramble: String,
  val comment: String
) {
  val timeWithPenalty: Double get() {
      return when (punishment) {
        Punishment.PLUS_TWO -> initialTime + 2
        Punishment.DNF -> Double.NaN
        Punishment.NONE -> initialTime
      }
    }

  fun atTime(time: IntRange) = timestamp % 86400000 in time

  override fun toString() = "Solve(initialTime=$initialTime, punishment=$punishment, timestamp=$timestamp, scramble='$scramble', comment='$comment')"
}
