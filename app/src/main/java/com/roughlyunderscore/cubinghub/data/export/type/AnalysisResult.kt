package com.roughlyunderscore.cubinghub.data.export.type

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.roughlyunderscore.cubinghub.R
import com.roughlyunderscore.cubinghub.data.UIText
import com.roughlyunderscore.cubinghub.theme.neutral_color_900
import com.roughlyunderscore.cubinghub.ui.model.AppViewModel
import com.roughlyunderscore.cubinghub.ui.util.elements.BentoRow
import com.roughlyunderscore.cubinghub.ui.util.elements.LargePopupWithText
import com.roughlyunderscore.cubinghub.ui.util.elements.LargeTableBentoBox
import com.roughlyunderscore.cubinghub.ui.util.elements.LongBentoBox
import com.roughlyunderscore.cubinghub.ui.util.elements.NarrowBentoColumn
import com.roughlyunderscore.cubinghub.ui.util.elements.SmallBentoBox
import com.roughlyunderscore.cubinghub.ui.util.elements.MediumPopupWithText
import com.roughlyunderscore.cubinghub.ui.util.elements.WideBentoColumn
import com.roughlyunderscore.cubinghub.ui.util.elements.getAnalyticStyle
import com.roughlyunderscore.cubinghub.ui.util.elements.getPleaseSelectFileStyle
import com.roughlyunderscore.cubinghub.util.atTime
import com.roughlyunderscore.cubinghub.util.solveAverage
import com.roughlyunderscore.cubinghub.util.breakIn
import com.roughlyunderscore.cubinghub.util.toTimeString
import com.roughlyunderscore.cubinghub.util.places
import com.roughlyunderscore.cubinghub.util.safeSubList
import com.roughlyunderscore.cubinghub.util.solveMean
import kotlin.math.sqrt
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class AnalysisResult(private val solves: MutableList<Solve>) {
  init {
    solves.removeIf {
      it.timeWithPenalty.isNaN() || it.scramble.isBlank() || it.timestamp < 100 || it.initialTime.isNaN() ||
        it.timeWithPenalty <= 0 || it.initialTime <= 0
    }
  }

  private val solvesAmount = solves.size

  private val meanTime = solves.map { it.timeWithPenalty }.filter { !it.isNaN() }.sum() / solvesAmount
  private val averageTime = solves.map { it.timeWithPenalty }.filter { !it.isNaN() }.average()

  private val bestSolve = solves.filter { !it.timeWithPenalty.isNaN() }.minByOrNull { it.timeWithPenalty }

  private val worstSolve = solves.filter { !it.timeWithPenalty.isNaN() }.maxByOrNull { it.timeWithPenalty }

  private val normalSolves = solves.filter { it.punishment == Punishment.NONE }.size
  private val plusTwoSolves = solves.filter { it.punishment == Punishment.PLUS_TWO }.size
  private val dnfSolves = solves.filter { it.punishment == Punishment.DNF }.size

  // I don't like the naming tbh
  private val solvesFrom0600To1159 = solves.filter { it.timestamp % 86400000 in 21600000..43199999 }
  private val solvesFrom1200To1759 = solves.filter { it.timestamp % 86400000 in 43200000..64799999 }
  private val solvesFrom1800To2359 = solves.filter { it.timestamp % 86400000 in 64800000..86399999 }
  private val solvesFrom0000To0559 = solves.filter { it.timestamp % 86400000 in 0..21599999 }

  private val bestSolveFrom0600To1159 = solvesFrom0600To1159.filter { !it.timeWithPenalty.isNaN() }.minByOrNull { it.timeWithPenalty }
  private val bestSolveFrom1200To1759 = solvesFrom1200To1759.filter { !it.timeWithPenalty.isNaN() }.minByOrNull { it.timeWithPenalty }
  private val bestSolveFrom1800To2359 = solvesFrom1800To2359.filter { !it.timeWithPenalty.isNaN() }.minByOrNull { it.timeWithPenalty }
  private val bestSolveFrom0000To0559 = solvesFrom0000To0559.filter { !it.timeWithPenalty.isNaN() }.minByOrNull { it.timeWithPenalty }

  private val mo3s = List(solves.size) { index ->
    solves.safeSubList(index, index + 3).takeIf { it.size == 3 }
  }.filterNotNull().map { solves ->
    solves.filter { solve -> !solve.timeWithPenalty.isNaN() }
  }

  private val ao5s = List(solves.size) { index ->
    solves.safeSubList(index, index + 5).takeIf { it.size == 5 }
  }.filterNotNull().map { solves ->
    solves.filter { solve -> !solve.timeWithPenalty.isNaN() }
  }

  private val ao12s = List(solves.size) { index ->
    solves.safeSubList(index, index + 12).takeIf { it.size == 12 }
  }.filterNotNull().map { solves ->
    solves.filter { solve -> !solve.timeWithPenalty.isNaN() }
  }

  private val ao50s = List(solves.size) { index ->
    solves.safeSubList(index, index + 50).takeIf { it.size == 50 }
  }.filterNotNull().map { solves ->
    solves.filter { solve -> !solve.timeWithPenalty.isNaN() }
  }

  private val ao100s = List(solves.size) { index ->
    solves.safeSubList(index, index + 100).takeIf { it.size == 100 }
  }.filterNotNull().map { solves ->
    solves.filter { solve -> !solve.timeWithPenalty.isNaN() }
  }

  private val bestMo3 = mo3s.filter { !it.solveAverage().isNaN() }.minByOrNull { it.solveAverage() }
  private val bestAo5 = ao5s.filter { !it.solveAverage().isNaN() }.minByOrNull { it.solveAverage() }
  private val bestAo12 = ao12s.filter { !it.solveAverage().isNaN() }.minByOrNull { it.solveAverage() }
  private val bestAo50 = ao50s.filter { !it.solveAverage().isNaN() }.minByOrNull { it.solveAverage() }
  private val bestAo100 = ao100s.filter { !it.solveAverage().isNaN() }.minByOrNull { it.solveAverage() }

  private val bestMo3From0600To1159 = mo3s.filter { mo3 -> mo3.atTime(21600000..43199999) }.map { it.solveAverage() }.filter { !it.isNaN() }.minOrNull()
  private val bestMo3From1200To1759 = mo3s.filter { mo3 -> mo3.atTime(43200000..64799999) }.map { it.solveAverage() }.filter { !it.isNaN() }.minOrNull()
  private val bestMo3From1800To2359 = mo3s.filter { mo3 -> mo3.atTime(64800000..86399999) }.map { it.solveAverage() }.filter { !it.isNaN() }.minOrNull()
  private val bestMo3From0000To0559 = mo3s.filter { mo3 -> mo3.atTime(0..21599999) }.map { it.solveAverage() }.filter { !it.isNaN() }.minOrNull()

  private val bestAo5From0600To1159 = ao5s.filter { ao5 -> ao5.atTime(21600000..43199999) }.map { it.solveAverage() }.filter { !it.isNaN() }.minOrNull()
  private val bestAo5From1200To1759 = ao5s.filter { ao5 -> ao5.atTime(43200000..64799999) }.map { it.solveAverage() }.filter { !it.isNaN() }.minOrNull()
  private val bestAo5From1800To2359 = ao5s.filter { ao5 -> ao5.atTime(64800000..86399999) }.map { it.solveAverage() }.filter { !it.isNaN() }.minOrNull()
  private val bestAo5From0000To0559 = ao5s.filter { ao5 -> ao5.atTime(0..21599999) }.map { it.solveAverage() }.filter { !it.isNaN() }.minOrNull()

  private val bestAo12From0600To1159 = ao12s.filter { ao12 -> ao12.atTime(21600000..43199999) }.map { it.solveAverage() }.filter { !it.isNaN() }.minOrNull()
  private val bestAo12From1200To1759 = ao12s.filter { ao12 -> ao12.atTime(43200000..64799999) }.map { it.solveAverage() }.filter { !it.isNaN() }.minOrNull()
  private val bestAo12From1800To2359 = ao12s.filter { ao12 -> ao12.atTime(64800000..86399999) }.map { it.solveAverage() }.filter { !it.isNaN() }.minOrNull()
  private val bestAo12From0000To0559 = ao12s.filter { ao12 -> ao12.atTime(0..21599999) }.map { it.solveAverage() }.filter { !it.isNaN() }.minOrNull()

  private val bestAo50From0600To1159 = ao50s.filter { ao50 -> ao50.atTime(21600000..43199999) }.map { it.solveAverage() }.filter { !it.isNaN() }.minOrNull()
  private val bestAo50From1200To1759 = ao50s.filter { ao50 -> ao50.atTime(43200000..64799999) }.map { it.solveAverage() }.filter { !it.isNaN() }.minOrNull()
  private val bestAo50From1800To2359 = ao50s.filter { ao50 -> ao50.atTime(64800000..86399999) }.map { it.solveAverage() }.filter { !it.isNaN() }.minOrNull()
  private val bestAo50From0000To0559 = ao50s.filter { ao50 -> ao50.atTime(0..21599999) }.map { it.solveAverage() }.filter { !it.isNaN() }.minOrNull()

  private val bestAo100From0600To1159 = ao100s.filter { ao100 -> ao100.atTime(21600000..43199999) }.map { it.solveAverage() }.filter { !it.isNaN() }.minOrNull()
  private val bestAo100From1200To1759 = ao100s.filter { ao100 -> ao100.atTime(43200000..64799999) }.map { it.solveAverage() }.filter { !it.isNaN() }.minOrNull()
  private val bestAo100From1800To2359 = ao100s.filter { ao100 -> ao100.atTime(64800000..86399999) }.map { it.solveAverage() }.filter { !it.isNaN() }.minOrNull()
  private val bestAo100From0000To0559 = ao100s.filter { ao100 -> ao100.atTime(0..21599999) }.map { it.solveAverage() }.filter { !it.isNaN() }.minOrNull()

  private val daysSinceFirstSolve = solves.sortedBy { it.timestamp }.let { it.last().timestamp - it.first().timestamp }.toDuration(DurationUnit.MILLISECONDS).inWholeDays

  private val confidentSubX = solves.sortedBy { it.timestamp }.takeLast(100).solveMean().let { mean ->
    mean + 1.96 * sqrt(mean / 100)
  }.places(2)

  private val totalTime = @Composable {
    StringBuilder().apply {
      val totalTime = solves.sumOf { it.initialTime }.toDuration(DurationUnit.SECONDS)

      val dayUnit = UIText.StringResource(R.string.days_unit).get()
      val hourUnit = UIText.StringResource(R.string.hours_unit).get()

      append("${totalTime.inWholeDays}$dayUnit").appendLine()
      append("${totalTime.inWholeHours}$hourUnit").appendLine()
    }.toString()
  }

  @Composable
  fun Visual(viewModel: AppViewModel) {
    val font = viewModel.fonts.akatab

    var worstScramblePopupDisplay by remember { mutableStateOf(false) }
    var bestScramblePopupDisplay by remember { mutableStateOf(false) }

    var bestMo3PopupDisplay by remember { mutableStateOf(false) }
    var bestAo5PopupDisplay by remember { mutableStateOf(false) }
    var bestAo12PopupDisplay by remember { mutableStateOf(false) }
    var bestAo50PopupDisplay by remember { mutableStateOf(false) }
    var bestAo100PopupDisplay by remember { mutableStateOf(false) }

    val randomScramble by remember { mutableStateOf(solves.random().scramble) }

    val bestMo3Data = buildAverageData(bestMo3)
    val bestAo5Data = buildAverageData(bestAo5)
    val bestAo12Data = buildAverageData(bestAo12)
    val bestAo50Data = buildAverageData(bestAo50)
    val bestAo100Data = buildAverageData(bestAo100)

    // Popups
    if (bestScramblePopupDisplay) {
      MediumPopupWithText(
        viewModel = viewModel, onDeny = { bestScramblePopupDisplay = false }, onDismiss = { bestScramblePopupDisplay = false },
        title = UIText.StringResource(R.string.best_scramble).get(), text = bestSolve?.scramble ?: ""
      )
    }

    if (worstScramblePopupDisplay) {
      MediumPopupWithText(
        viewModel = viewModel, onDeny = { worstScramblePopupDisplay = false }, onDismiss = { worstScramblePopupDisplay = false },
        title = UIText.StringResource(R.string.worst_scramble).get(), text = worstSolve?.scramble ?: ""
      )
    }

    if (bestMo3PopupDisplay) {
      LargePopupWithText(
        viewModel = viewModel, onDeny = { bestMo3PopupDisplay = false }, onDismiss = { bestMo3PopupDisplay = false },
        title = UIText.StringResource(R.string.best_mo3_data).get(),
        text = bestMo3Data
      )
    }

    if (bestAo5PopupDisplay) {
      LargePopupWithText(
        viewModel = viewModel, onDeny = { bestAo5PopupDisplay = false }, onDismiss = { bestAo5PopupDisplay = false },
        title = UIText.StringResource(R.string.best_ao5_data).get(),
        text = bestAo5Data
      )
    }

    if (bestAo12PopupDisplay) {
      LargePopupWithText(
        viewModel = viewModel, onDeny = { bestAo12PopupDisplay = false }, onDismiss = { bestAo12PopupDisplay = false },
        title = UIText.StringResource(R.string.best_ao12_data).get(),
        text = bestAo12Data
      )
    }

    if (bestAo50PopupDisplay) {
      LargePopupWithText(
        viewModel = viewModel, onDeny = { bestAo50PopupDisplay = false }, onDismiss = { bestAo50PopupDisplay = false },
        title = UIText.StringResource(R.string.best_ao50_data).get(),
        text = bestAo50Data
      )
    }

    if (bestAo100PopupDisplay) {
      LargePopupWithText(
        viewModel = viewModel, onDeny = { bestAo100PopupDisplay = false }, onDismiss = { bestAo100PopupDisplay = false },
        title = UIText.StringResource(R.string.best_ao100_data).get(),
        text = bestAo100Data
      )
    }

    // Multiple rows because I couldn't come up with a better idea for how
    // to do a good bento box design

    // Row 1:
    // Column 1: Total solves; mean/average
    // Column 2: Solve statistics

    // Row 2:
    // Column 1: Daytime distribution
    // Column 2: Best solve; worst solve

    // Row 3:
    // Column 1: Best mo3; best ao5
    // Column 2: Best solve time distribution

    // Row 4:
    // Column 1: Best ao5 distribution
    // Column 2: Best ao12; best ao50

    // Row 5:
    // Column 1: Best ao100; Days since first solve
    // Column 2: Best ao12 distribution

    // Row 6:
    // Column 1: Best mo3 distribution
    // Column 2: Interactable "are you sub-x"

    // Row 7:
    // Column 1: Random scramble
    // Column 2: Best ao50 distribution

    // Row 8:
    // Column 1: Best ao100 distribution
    // Column 2: Total time cubing


    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
    ) {
      // Row 1
      BentoRow {
        // Column 1
        NarrowBentoColumn(isLeft = true) {
          // Total solves
          SmallBentoBox {
            Text(
              text = UIText.StringResource(R.string.total_solves).get(),
              style = getPleaseSelectFileStyle(font).copy(fontSize = 14.sp)
            )

            Text(
              text = solvesAmount.breakIn(3),
              style = getAnalyticStyle(font)
            )
          }

          SmallBentoBox {
            Text(
              text = UIText.StringResource(R.string.mean_average).get(),
              style = getPleaseSelectFileStyle(font).copy(fontSize = 14.sp)
            )

            Text(
              text = "${meanTime.places(2)}/${averageTime.places(2)}",
              style = getAnalyticStyle(font)
            )
          }
        }

        // Column 2
        WideBentoColumn(isLeft = false) {
          // Solve statistics
          LargeTableBentoBox(
            contentLeft = {
              Text(
                text = UIText.StringResource(R.string.ok).get(),
                style = getAnalyticStyle(font).copy(color = neutral_color_900)
              )

              Text(
                text = UIText.StringResource(R.string.plus_two).get(),
                style = getAnalyticStyle(font).copy(color = neutral_color_900)
              )

              Text(
                text = UIText.StringResource(R.string.dnf).get(),
                style = getAnalyticStyle(font).copy(color = neutral_color_900)
              )
            },

            contentRight = {
              Text(
                text = normalSolves.breakIn(3),
                style = getAnalyticStyle(font)
              )

              Text(
                text = plusTwoSolves.breakIn(3),
                style = getAnalyticStyle(font)
              )

              Text(
                text = dnfSolves.breakIn(3),
                style = getAnalyticStyle(font)
              )
            },

            title = UIText.StringResource(R.string.solve_statistics).get(),
            font = font
          )
        }
      }

      // Row 2
      BentoRow {
        // Column 1
        DistributionTable(
          true,
          solvesFrom0600To1159.size.breakIn(3), solvesFrom1200To1759.size.breakIn(3),
          solvesFrom1800To2359.size.breakIn(3), solvesFrom0000To0559.size.breakIn(3),
          UIText.StringResource(R.string.daytime_distribution).get(), font
        )

        // Column 2
        NarrowBentoColumn(isLeft = false) {
          // Total solves
          SmallBentoBox(onClick = { bestScramblePopupDisplay = true }) {
            Text(
              text = UIText.StringResource(R.string.best_solve).get(),
              style = getPleaseSelectFileStyle(font).copy(fontSize = 14.sp)
            )

            Text(
              text = (bestSolve?.timeWithPenalty ?: 0.0).toTimeString(),
              style = getAnalyticStyle(font)
            )
          }

          SmallBentoBox(onClick = { worstScramblePopupDisplay = true }) {
            Text(
              text = UIText.StringResource(R.string.worst_solve).get(),
              style = getPleaseSelectFileStyle(font).copy(fontSize = 14.sp)
            )

            Text(
              text = (worstSolve?.timeWithPenalty ?: 0.0).toTimeString(),
              style = getAnalyticStyle(font)
            )
          }
        }
      }

      // Row 3
      BentoRow {
        // Column 1
        NarrowBentoColumn(isLeft = true) {
          // Total solves
          SmallBentoBox(onClick = { bestMo3PopupDisplay = true }) {
            Text(
              text = UIText.StringResource(R.string.best_mo3).get(),
              style = getPleaseSelectFileStyle(font).copy(fontSize = 14.sp)
            )

            Text(
              text = bestMo3?.solveAverage().places(2),
              style = getAnalyticStyle(font)
            )
          }

          SmallBentoBox(onClick = { bestAo5PopupDisplay = true }) {
            Text(
              text = UIText.StringResource(R.string.best_ao5).get(),
              style = getPleaseSelectFileStyle(font).copy(fontSize = 14.sp)
            )

            Text(
              text = bestAo5?.solveAverage().places(2),
              style = getAnalyticStyle(font)
            )
          }
        }

        // Column 2
        DistributionTable(
          false,
          bestSolveFrom0600To1159,
          bestSolveFrom1200To1759,
          bestSolveFrom1800To2359,
          bestSolveFrom0000To0559,
          UIText.StringResource(R.string.best_solves_daytime).get(),
          font
        )
      }

      // Row 4
      BentoRow {
        // Column 1
        DistributionTable(
          true,
          bestAo5From0600To1159.toTimeString(), bestAo5From1200To1759.toTimeString(),
          bestAo5From1800To2359.toTimeString(), bestAo5From0000To0559.toTimeString(),
          UIText.StringResource(R.string.best_ao5_daytime).get(), font
        )

        // Column 2
        NarrowBentoColumn(isLeft = false) {
          // Total solves
          SmallBentoBox(onClick = { bestAo12PopupDisplay = true }) {
            Text(
              text = UIText.StringResource(R.string.best_ao12).get(),
              style = getPleaseSelectFileStyle(font).copy(fontSize = 14.sp)
            )

            Text(
              text = bestAo12?.solveAverage().places(2),
              style = getAnalyticStyle(font)
            )
          }

          SmallBentoBox(onClick = { bestAo50PopupDisplay = true }) {
            Text(
              text = UIText.StringResource(R.string.best_ao50).get(),
              style = getPleaseSelectFileStyle(font).copy(fontSize = 14.sp)
            )

            Text(
              text = bestAo50?.solveAverage().places(2),
              style = getAnalyticStyle(font)
            )
          }
        }
      }

      // Row 5
      BentoRow {
        // Column 1
        NarrowBentoColumn(isLeft = true) {
          // Total solves
          SmallBentoBox(onClick = { bestAo100PopupDisplay = true }) {
            Text(
              text = UIText.StringResource(R.string.best_ao100).get(),
              style = getPleaseSelectFileStyle(font).copy(fontSize = 14.sp)
            )

            Text(
              text = bestAo100?.solveAverage().places(2),
              style = getAnalyticStyle(font)
            )
          }

          SmallBentoBox {
            Text(
              text = UIText.StringResource(R.string.days_since_first_solve).get(),
              style = getPleaseSelectFileStyle(font).copy(fontSize = 14.sp)
            )

            Text(
              text = daysSinceFirstSolve.toString(),
              style = getAnalyticStyle(font)
            )
          }
        }

        // Column 2
        DistributionTable(
          false,
          bestAo12From0600To1159.toTimeString(), bestAo12From1200To1759.toTimeString(),
          bestAo12From1800To2359.toTimeString(), bestAo12From0000To0559.toTimeString(),
          UIText.StringResource(R.string.best_ao12_daytime).get(), font
        )
      }

      // Row 6
      BentoRow {
        // Column 1
        DistributionTable(
          true,
          bestMo3From0600To1159.toTimeString(), bestMo3From1200To1759.toTimeString(),
          bestMo3From1800To2359.toTimeString(), bestMo3From0000To0559.toTimeString(),
          UIText.StringResource(R.string.best_mo3_daytime).get(), font
        )

        // Column 2
        NarrowBentoColumn(isLeft = false) {
          // Total solves
          LongBentoBox {
            Text(
              text = UIText.StringResource(R.string.you_are_at_least_sub).get(),
              style = getPleaseSelectFileStyle(font).copy(fontSize = 14.sp)
            )

            Text(
              text = confidentSubX,
              style = getAnalyticStyle(font)
            )
          }
        }
      }

      // Row 7
      BentoRow {
        // Column 1
        NarrowBentoColumn(isLeft = true) {
          // Total solves
          LongBentoBox(scrollable = true) {
            Text(
              text = UIText.StringResource(R.string.random_scramble_from_export).get(),
              style = getPleaseSelectFileStyle(font).copy(fontSize = 14.sp)
            )

            Text(
              text = randomScramble,
              style = getAnalyticStyle(font).copy(fontSize = 14.sp)
            )
          }
        }

        // Column 2
        DistributionTable(
          false,
          bestAo50From0600To1159.toTimeString(), bestAo50From1200To1759.toTimeString(),
          bestAo50From1800To2359.toTimeString(), bestAo50From0000To0559.toTimeString(),
          UIText.StringResource(R.string.best_ao50_daytime).get(), font
        )
      }

      // Row 8
      BentoRow {
        // Column 1
        DistributionTable(
          true,
          bestAo100From0600To1159.toTimeString(), bestAo100From1200To1759.toTimeString(),
          bestAo100From1800To2359.toTimeString(), bestAo100From0000To0559.toTimeString(),
          UIText.StringResource(R.string.best_ao100_daytime).get(), font
        )

        // Column 2
        NarrowBentoColumn(isLeft = false) {
          // Total solves
          LongBentoBox {
            Text(
              text = UIText.StringResource(R.string.total_time).get(),
              style = getPleaseSelectFileStyle(font).copy(fontSize = 14.sp)
            )

            Text(
              text = totalTime.invoke(),
              style = getAnalyticStyle(font)
            )
          }
        }
      }

      Divider(color = neutral_color_900)
    }
  }

  private fun buildAverageData(solves: List<Solve>?): String =
    StringBuilder().apply {
      solves?.forEach { solve ->
        append("${solve.timeWithPenalty}: ${solve.scramble}").appendLine()
      }
    }.toString()

  @Composable
  private fun DistributionTable(
    isLeft: Boolean,
    from0600To1159: List<Solve>?,
    from1200To1759: List<Solve>?,
    from1800To2359: List<Solve>?,
    from0000To0059: List<Solve>?,
    title: String,
    font: FontFamily
  ) {
    DistributionTable(
      isLeft,
      from0600To1159?.solveAverage().toTimeString(),
      from1200To1759?.solveAverage().toTimeString(),
      from1800To2359?.solveAverage().toTimeString(),
      from0000To0059?.solveAverage().toTimeString(),
      title, font
    )
  }

  @Composable
  private fun DistributionTable(
    isLeft: Boolean,
    from0600To1159: Solve?,
    from1200To1759: Solve?,
    from1800To2359: Solve?,
    from0000To0059: Solve?,
    title: String,
    font: FontFamily
  ) {
    DistributionTable(
      isLeft,
      from0600To1159?.timeWithPenalty.toTimeString(),
      from1200To1759?.timeWithPenalty.toTimeString(),
      from1800To2359?.timeWithPenalty.toTimeString(),
      from0000To0059?.timeWithPenalty.toTimeString(),
      title, font
    )
  }

  @Composable
  private fun DistributionTable(
    isLeft: Boolean,
    valueFrom0600To1159: String?,
    valueFrom1200To1759: String?,
    valueFrom1800To2359: String?,
    valueFrom0000To0059: String?,
    title: String,
    font: FontFamily
  ) {
    WideBentoColumn(isLeft) {
      // Solve statistics
      LargeTableBentoBox(
        contentLeft = {
          Text(
            text = UIText.StringResource(R.string.from_0600_to_1159).get(),
            style = getAnalyticStyle(font).copy(color = neutral_color_900)
          )

          Text(
            text = UIText.StringResource(R.string.from_1200_to_1759).get(),
            style = getAnalyticStyle(font).copy(color = neutral_color_900)
          )

          Text(
            text = UIText.StringResource(R.string.from_1800_to_2359).get(),
            style = getAnalyticStyle(font).copy(color = neutral_color_900)
          )

          Text(
            text = UIText.StringResource(R.string.from_0000_to_0559).get(),
            style = getAnalyticStyle(font).copy(color = neutral_color_900)
          )
        },

        contentRight = {
          Text(
            text = valueFrom0600To1159.toString(),
            style = getAnalyticStyle(font)
          )

          Text(
            text = valueFrom1200To1759.toString(),
            style = getAnalyticStyle(font)
          )

          Text(
            text = valueFrom1800To2359.toString(),
            style = getAnalyticStyle(font)
          )

          Text(
            text = valueFrom0000To0059.toString(),
            style = getAnalyticStyle(font)
          )
        },

        title = title,
        font = font
      )
    }
  }
}