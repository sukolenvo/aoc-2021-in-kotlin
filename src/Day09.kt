object Day09 {

  @JvmStatic
  fun main(args: Array<String>) {
    fun part1(input: Array<Array<Int>>): Int {
      var result = 0
      for (row in input.indices) {
        for (column in input[row].indices) {
          if (row > 0 && input[row - 1][column] <= input[row][column]) {
            continue
          }
          if (column > 0 && input[row][column - 1] <= input[row][column]) {
            continue
          }
          if (row < input.size - 1 && input[row + 1][column] <= input[row][column]) {
            continue
          }
          if (column < input[0].size - 1 && input[row][column + 1] <= input[row][column]) {
            continue
          }
          result += input[row][column] + 1
        }
      }
      return result
    }

    fun measureBasin(input: Array<Array<Int>>, row: Int, column: Int): Int {
      val basin = mutableSetOf<Pair<Int, Int>>()
      val tasks = mutableListOf(Pair(row, column))
      while (tasks.isNotEmpty()) {
        val rowColumn = tasks.removeAt(0)
        if (basin.add(rowColumn)) {
          if (rowColumn.first > 0
            && input[rowColumn.first - 1][rowColumn.second] < 9
            && input[rowColumn.first - 1][rowColumn.second] > input[rowColumn.first][rowColumn.second]) {
            tasks.add(Pair(rowColumn.first - 1, rowColumn.second))
          }
          if (rowColumn.second > 0
            && input[rowColumn.first][rowColumn.second - 1] < 9
            && input[rowColumn.first][rowColumn.second - 1] > input[rowColumn.first][rowColumn.second]) {
            tasks.add(Pair(rowColumn.first, rowColumn.second - 1))
          }
          if (rowColumn.first < input.size - 1
            && input[rowColumn.first + 1][rowColumn.second] < 9
            && input[rowColumn.first + 1][rowColumn.second] > input[rowColumn.first][rowColumn.second]) {
            tasks.add(Pair(rowColumn.first + 1, rowColumn.second))
          }
          if (rowColumn.second < input.size - 1
            && input[rowColumn.first][rowColumn.second + 1] < 9
            && input[rowColumn.first][rowColumn.second + 1] > input[rowColumn.first][rowColumn.second]) {
            tasks.add(Pair(rowColumn.first, rowColumn.second + 1))
          }
        }
      }
      return basin.size
    }

    fun part2(input: Array<Array<Int>>): Int {
      val basins = mutableListOf<Int>()
      for (row in input.indices) {
        for (column in input[row].indices) {
          if (row > 0 && input[row - 1][column] <= input[row][column]) {
            continue
          }
          if (column > 0 && input[row][column - 1] <= input[row][column]) {
            continue
          }
          if (row < input.size - 1 && input[row + 1][column] <= input[row][column]) {
            continue
          }
          if (column < input[0].size - 1 && input[row][column + 1] <= input[row][column]) {
            continue
          }
          basins.add(measureBasin(input, row, column))
        }
      }
      return basins.sorted().takeLast(3).reduce { l, r -> l * r}
    }

    val input = readInput(javaClass.name)
    val matrix = input.map { it.toCharArray().map { it.digitToInt() }.toTypedArray() }.toTypedArray()
    println(part1(matrix))
    println(part2(matrix))
  }
}