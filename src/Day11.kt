import java.util.stream.IntStream
import kotlin.math.max
import kotlin.math.min

object Day11 {

  fun part1(input: Array<Array<Int>>): Int {
    return (0 until 100).sumOf { runStep(input) }
  }

  fun runStep(input: Array<Array<Int>>): Int {
    var result = 0
    for (row in input.indices) {
      for (column in input[row].indices) {
        result += increaseLevel(input, row, column)
      }
    }
    for (row in input.indices) {
      for (column in input[row].indices) {
        if (input[row][column] >= 10) {
          input[row][column] = 0
        }
      }
    }
    return result
  }

  fun increaseLevel(input: Array<Array<Int>>, row: Int, column: Int): Int {
    var result = 0
    input[row][column]++
    if (input[row][column] == 10) {
      result++
      for (i in max(0, row - 1)..min(input.size - 1, row + 1)) {
        for (j in max(0, column - 1)..min(input.size - 1, column + 1)) {
          if (i != row || j != column) {
            result += increaseLevel(input, i, j)
          }
        }
      }
    }
    return result
  }

  fun part2(input: Array<Array<Int>>): Int {
    return IntStream.iterate(1) { it + 1 }
      .filter { runStep(input) == input.size * input.size }
      .findFirst()
      .orElseThrow()
  }

  @JvmStatic
  fun main(args: Array<String>) {
    val input = readInput(javaClass.name)
    println(part1(input.map { it.toCharArray().map { it.digitToInt() }.toTypedArray() }.toTypedArray()))
    println(part2(input.map { it.toCharArray().map { it.digitToInt() }.toTypedArray() }.toTypedArray()))
  }
}
