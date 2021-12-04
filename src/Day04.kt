import java.util.regex.Pattern

typealias Matrix = Array<IntArray>

fun main() {
  fun calcAnswer(matrix: Matrix, number: Int) = matrix.flatMap { it.asSequence() }
    .filter { it != -1 }
    .sum() * number

  fun part1(sequence: List<Int>, matrices: List<Matrix>): Int {
    for (number in sequence) {
      for (matrix in matrices) {
        for (row in matrix) {
          for (index in row.indices) {
            if (row[index] == number) {
              row[index] = -1
              if (row.all { it == -1 } || matrix.map { it[index] }.all { it == -1 }) {
                return calcAnswer(matrix, number)
              }
            }
          }
        }
      }
    }
    throw Error("Winner not found")
  }

  fun part2(sequence: List<Int>, matrices: List<Matrix>): Int {
    val completed = mutableSetOf<Any>()
    for (number in sequence) {
      for (matrix in matrices) {
        for (row in matrix) {
          if (completed.contains(matrix)) {
            break
          }
          for (index in row.indices) {
            if (row[index] == number) {
              row[index] = -1
              if (row.all { it == -1 } || matrix.map { it[index] }.all { it == -1 }) {
                completed.add(matrix)
                if (completed.size == matrices.size) {
                  return calcAnswer(matrix, number)
                }
                break
              }
            }
          }
        }
      }
    }
    throw Error("Winner not found")
  }

  val input = readInput("Day04")
  val sequence = input[0].split(",").map { it.toInt() }
  val matrices = mutableListOf<Matrix>()
  for (i in 0 until input.size / 6) {
    val matrix: Matrix = Array(5) { IntArray(5) }
    for (row in 1..5) {
      matrix[row - 1] = input[row + i * 6 + 1].trim().split(Pattern.compile("\\s+")).map { it.toInt() }.toIntArray()
    }
    matrices.add(matrix)
  }
  // println(part1(sequence, matrices))
  println(part2(sequence, matrices))
}
