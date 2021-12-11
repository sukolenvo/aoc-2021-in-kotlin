import kotlin.math.ceil
import kotlin.math.floor

data class CrabsPosition(var position: Int, var count: Int)
data class CrabFuel(var position: Int, var nextFuelUsage: Int = 1) {

  fun fuelTill(position: Double): Int {
    if (this.position < position) {
      val steps = ceil(position).toInt() - this.position
      return (1..steps).sumOf { it + nextFuelUsage - 1 }
    }
    val steps = this.position - floor(position).toInt()
    return (1..steps).sumOf { it + nextFuelUsage - 1 } * -1
  }
}

fun main() {

  fun part1(input: List<Int>): Int {
    val positions = input.groupingBy { it }.eachCount().map { CrabsPosition(it.key, it.value) }.sortedBy { it.position }.toMutableList()
    var fuelUsed = 0
    while (positions.size > 1) {
      val geoCenter = (positions.last().position + positions.first().position).toDouble() / 2
      val weightedCenter = positions.sumOf { it.position * it.count }.toDouble() / input.size
      if (geoCenter < weightedCenter) {
        if (positions.first().position + 1 == positions[1].position) {
          val moved = positions.removeFirst()
          positions.first().count += moved.count
          fuelUsed += moved.count
        } else {
          positions.first().position++
          fuelUsed += positions.first().count
        }
      } else {
        if (positions.last().position - 1 == positions[positions.size - 2].position) {
          val moved = positions.removeLast()
          positions.last().count += moved.count
          fuelUsed += moved.count
        } else {
          positions.last().position--
          fuelUsed += positions.last().count
        }
      }
    }
    return fuelUsed
  }

  fun part2(input: List<Int>): Int {
    val positions = input.map { CrabFuel(it) }.sortedBy { it.position }.toMutableList()
    var fuelUsed = 0
    while (positions.first().position != positions.last().position) {
      val geoCenter = (positions.last().position + positions.first().position).toDouble() / 2
      val moveRight = positions.sumOf {
        it.fuelTill(geoCenter)
      }.toDouble() < 0
      if (moveRight) {
        val toMove = positions.takeWhile { it.position == positions.first().position }
        fuelUsed += toMove.sumOf { it.nextFuelUsage }
        toMove.forEach { it.position++; it.nextFuelUsage++ }
      } else {
        val toMove = positions.takeLastWhile { it.position == positions.last().position }
        fuelUsed += toMove.sumOf { it.nextFuelUsage }
        toMove.forEach { it.position--; it.nextFuelUsage++ }
      }
    }
    return fuelUsed
  }

  val input = readInput("Day07")
  val crabs = input[0].split(",").map { it.toInt() }
  println(part1(crabs))
  println(part2(crabs))
}