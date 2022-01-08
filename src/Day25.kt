object Day25 {

  val EAST_CUCUMBER = '>'
  val SOUTH_CUCUMBER = 'v'
  val EMPTY_SPACE = '.'

  fun part1(map: Array<CharArray>): Int {
    for (i in 1..1000) {
      if (runStep(map) == 0) {
        return i
      }
    }
    throw Error("Solution not found")
  }

  private fun runStep(map: Array<CharArray>): Int {
    var moveEast = mutableListOf<Pair<Int, Int>>()
    for (row in map.indices) {
      for (column in map[row].indices) {
        if (map[row][column] == EAST_CUCUMBER && map[row][(column + 1).mod(map[row].size)] == EMPTY_SPACE) {
          moveEast.add(Pair(row, column))
        }
      }
    }
    moveEast.forEach { (row, column) ->
      map[row][column] = EMPTY_SPACE
      map[row][(column + 1).mod(map[row].size)] = EAST_CUCUMBER
    }
    var moveSouth = mutableListOf<Pair<Int, Int>>()
    for (row in map.indices) {
      for (column in map[row].indices) {
        if (map[row][column] == SOUTH_CUCUMBER && map[(row + 1).mod(map.size)][column] == EMPTY_SPACE) {
          moveSouth.add(Pair(row, column))
        }
      }
    }
    moveSouth.forEach { (row, column) ->
      map[row][column] = EMPTY_SPACE
      map[(row + 1).mod(map.size)][column] = SOUTH_CUCUMBER
    }
    return moveSouth.size + moveEast.size
  }

  fun part2(input: List<String>): Int {
    return 0
  }

  @JvmStatic
  fun main(args: Array<String>) {
    val input = readInput(javaClass.name)
    val map = input.map { it.toCharArray() }.toTypedArray()
    println(part1(map))
    println(part2(input))
  }
}
