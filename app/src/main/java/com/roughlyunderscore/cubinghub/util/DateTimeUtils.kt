package com.roughlyunderscore.cubinghub.util

import java.util.Calendar
import java.util.TimeZone

fun String.csTimeStringToTimestamp(): Long {
  // 2021-09-20 23:36:36
  val dateTimeSplit = this.split(" ")

  val dateSplit = dateTimeSplit[0].split("-")
  val timeSplit = dateTimeSplit[1].split(":")

  val year = dateSplit[0].toInt()
  val month = dateSplit[1].toInt()
  val day = dateSplit[2].toInt()

  val hour = timeSplit[0].toInt()
  val minute = timeSplit[1].toInt()
  val second = timeSplit[2].toInt()

  return Calendar.getInstance().apply {
    set(year, month, day, hour, minute, second)
  }.timeInMillis
}

fun String.twistyTimeStringToTimestamp(): Long {
  // Convert 2023-06-16T22:56:52.181+05:00 to Long
  val dateTimeSplit = this.split("T")

  val dateSplit = dateTimeSplit[0].split("-")
  val timeFullSplit = dateTimeSplit[1].split("[\\+-]".toRegex())

  val timeSplit = timeFullSplit[0].split(":")
  val timeZone = timeFullSplit[1]
  val timeZoneSign = if (dateTimeSplit.contains("+")) "+" else "-"

  val year = dateSplit[0].toInt()
  val month = dateSplit[1].toInt()
  val day = dateSplit[2].toInt()

  val hour = timeSplit[0].toInt()
  val minute = timeSplit[1].toInt()
  val second = timeSplit[2].split(".")[0].toInt()

  return Calendar.getInstance().apply {
    set(year, month, day, hour, minute, second)
    setTimeZone(TimeZone.getTimeZone("GMT$timeZoneSign$timeZone"))

  }.timeInMillis
}