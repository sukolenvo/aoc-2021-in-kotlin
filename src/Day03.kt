fun main() {
  fun part1(input: List<String>): Int {
    var min = ""
    var max = ""
    for (i in 0 until input[0].length) {
      val ones = input.filter { it[i] == '1' }.count()
      min += if (ones < input.size / 2) "1" else "0"
      max += if (ones > input.size / 2) "1" else "0"
      check(ones != input.size / 2)
    }
    return min.toInt(2) * max.toInt(2)
  }

  fun part2(input: List<String>): Int {
    var max = input.toList()
    var index = 0
    val comparator = compareBy<List<String>> { it.size }.thenBy { it[0][index] }
    while (max.size > 1) {
      val groupBy = max.groupBy { it[index] }
      max = groupBy
        .values
        .sortedWith(comparator)
        .last()
      index++
    }
    var min = input
    index = 0
    while (min.size > 1) {
      min = min.groupBy { it[index] }
        .values
        .sortedWith(comparator)
        .first()
      index++
    }
    return max[0].toInt(2) * min[0].toInt(2)
  }

  val input = readInput("Day03")
  println(part1(input))
  println(part2(input))
}