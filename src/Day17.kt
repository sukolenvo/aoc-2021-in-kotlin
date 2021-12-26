import kotlin.math.absoluteValue

object Day17 {

  fun part1(xMin: Int, xMax: Int, yMin: Int, yMax: Int): Int {
    return (1..(yMin.absoluteValue * 2 + 2)).mapNotNull { calcHeight(xMin, xMax, yMin, yMax, it) }.maxOf { it }
  }

  fun calcHeight(xMin: Int, xMax: Int, yMin: Int, yMax: Int, steps: Int): Int? {
    val initialY = (yMax.toDouble() / steps + (steps.toDouble() - 1) / 2).toInt()
    val targetY = initialY * steps - steps * (steps - 1) / 2
    if (targetY < yMin || targetY > yMax) {
      return null
    }
    val maxInitialSpeed = (xMax.toDouble() / steps + (steps.toDouble() - 1) / 2).toInt()
    val xTargetMatched = (maxInitialSpeed downTo 1).any { initialX ->
      val targetX = (0 until steps).map { initialX - it }.takeWhile { it > 0 }.sum()
      targetX in xMin..xMax
    }
    if (!xTargetMatched) {
      return null
    }
    return (1..initialY).sum()
  }

  fun part2(xMin: Int, xMax: Int, yMin: Int, yMax: Int): Int {
    return (1..(yMin.absoluteValue * 2 + 2)).flatMap { velocities(xMin, xMax, yMin, yMax, it) }.distinct().count()
  }

  fun velocities(xMin: Int, xMax: Int, yMin: Int, yMax: Int, steps: Int): List<Pair<Int, Int>> {
    val maxInitialY = (yMax.toDouble() / steps + (steps.toDouble() - 1) / 2).toInt()
    val yList = (maxInitialY downTo yMin).filter { initialY ->
      val targetY = initialY * steps - steps * (steps - 1) / 2
      targetY in yMin..yMax
    }
    if (yList.isEmpty()) {
      return listOf()
    }
    val maxInitialX = (xMax.toDouble() / steps + (steps.toDouble() - 1) / 2).toInt()
    val xList = (maxInitialX downTo 1).filter { initialX ->
      val targetX = (0 until steps).map { initialX - it }.takeWhile { it > 0 }.sum()
      targetX in xMin..xMax
    }
    return yList.flatMap { y ->  xList.map { Pair(it, y) } }
  }

  @JvmStatic
  fun main(args: Array<String>) {
    val input = readInput(javaClass.name)
    val words = input[0].split(" ")
    val xRange = words[2].replace("x=", "").replace(",", "").split("..").map { it.toInt() }
    val yRange = words[3].replace("y=", "").split("..").map { it.toInt() }
    println(part1(xRange[0], xRange[1], yRange[0], yRange[1]))
    println(part2(xRange[0], xRange[1], yRange[0], yRange[1]))
  }
}
