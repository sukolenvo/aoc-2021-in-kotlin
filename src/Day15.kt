import java.util.PriorityQueue

object Day15 {

  fun part1(input: Array<Array<Int>>): Int {
    val minPath = Array(input.size) { Array(input.size) { 0 } }
    minPath[0][0] = -1
    val candidateMoves = PriorityQueue<Triple<Int, Int, Int>> { l, r -> l.first.compareTo(r.first) }
    candidateMoves.add(Triple(input[0][1], 0, 1))
    candidateMoves.add(Triple(input[1][0], 1, 0))
    while (minPath[input.size - 1][input.size - 1] == 0) {
      val move = candidateMoves.poll()
      if (minPath[move.second][move.third] == 0) {
        minPath[move.second][move.third] = move.first
        if (move.second > 0 && minPath[move.second - 1][move.third] == 0) {
          candidateMoves.add(Triple(move.first + input[move.second - 1][move.third], move.second - 1, move.third))
        }
        if (move.third > 0 && minPath[move.second][move.third - 1] == 0) {
          candidateMoves.add(Triple(move.first + input[move.second][move.third - 1], move.second, move.third - 1))
        }
        if (move.second < input.size - 1 && minPath[move.second + 1][move.third] == 0) {
          candidateMoves.add(Triple(move.first + input[move.second + 1][move.third], move.second + 1, move.third))
        }
        if (move.third < input.size - 1 && minPath[move.second][move.third + 1] == 0) {
          candidateMoves.add(Triple(move.first + input[move.second][move.third + 1], move.second, move.third + 1))
        }
      }
    }
    return minPath[input.size - 1][input.size - 1]
  }

  fun part2(input: Array<Array<Int>>): Int {
    val matrix = Array(input.size * 5) { Array(input.size * 5) { 0 } }
    for (i in 0 until 5) {
      for (j in 0 until 5) {
        for (m in input.indices) {
          for (n in input.indices) {
            matrix[input.size * i + m][input.size * j + n] = (input[m][n] + i + j - 1) % 9 + 1
          }
        }
      }
    }
    return part1(matrix)
  }

  @JvmStatic
  fun main(args: Array<String>) {
    val input = readInput(javaClass.name)
    val matrix = input.map { it.toCharArray().map { it.digitToInt() }.toTypedArray() }.toTypedArray()
    println(part1(matrix))
    println(part2(matrix))
  }
}
