package com.roughlyunderscore.cubinghub.util

import java.text.DecimalFormat

val normalizationRegex = Regex("[-_?*: ]")

fun String.normalize() = this.replace(normalizationRegex, "").lowercase()

/**
 * Converts a time in seconds to a string in the format "hh:mm:ss.SS".
 */
fun Double?.toTimeString(): String {
  if (this == null) return "NaN"

  // 66099.26
  val hours = (this.toInt() / 3600).let {
    if (it == 0) "" else "$it:"
  }

  val minutes = ((this.toInt() % 3600) / 60).let {
    if (it == 0 && hours.isBlank()) "" else "$it:".padStart(3, '0')
  }

  val seconds = (this.toInt() % 60).let {
    if (minutes.isBlank()) "$it" else "$it".padStart(2, '0')
  }

  val milliseconds =
    (DecimalFormat("#.##")
      .format(this - this
        .toInt()
        .toDouble()
    ).toDouble() * 100)
    .toInt()
    .toString()
    .padStart(2, '0')

  return "$hours$minutes${seconds.padStart(2, '0')}.$milliseconds".let {
    if (it[0] == '0' && it[1] != '.') it.substring(1)
    else it
  }.let {
    if (it.split(".").last().length == 3) it.take(it.length - 1)
    else it
  }
}

/**
 * Strips the beginning of a string of a [char].
 */
fun String.stripStart(char: Char): String {
  var str = this
  while (str.isNotEmpty() && str[0] == char) str = str.substring(1)
  return str
}

/**
 * Breaks an integer into pieces with size of [pieceSize].
 */
fun Int.breakIn(pieceSize: Int): String = this.toString().reversed().chunked(pieceSize).reversed().joinToString(" ") { it.reversed() }

fun Double?.places(places: Int) = if (this == null) "null" else "%.${places}f".format(this)
