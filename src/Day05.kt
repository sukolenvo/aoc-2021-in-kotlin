fun main() {
  fun part1(input: List<Line>): Int {
    val maxX = input.flatMap { listOf(it.startX, it.endX) }
      .maxOrNull()!!
    val maxY = input.flatMap { listOf(it.startY, it.endY) }
      .maxOrNull()!!
    val field = Array(maxY + 1) { IntArray(maxX + 1) }
    input.filter { it.startX == it.endX || it.startY == it.endY }.forEach {
      for (row in it.rows()) {
        for (column in it.columns()) {
          field[row][column]++
        }
      }
    }
    return field.flatMap { it.asSequence() }.count { it >= 2 }
  }

  fun part2(input: List<Line>): Int {
    val maxX = input.flatMap { listOf(it.startX, it.endX) }
      .maxOrNull()!!
    val maxY = input.flatMap { listOf(it.startY, it.endY) }
      .maxOrNull()!!
    val field = Array(maxY + 1) { IntArray(maxX + 1) }
    input.filter { it.startX == it.endX || it.startY == it.endY }.forEach {
      for (row in it.rows()) {
        for (column in it.columns()) {
          field[row][column]++
        }
      }
    }
    input.filter { it.startX != it.endX && it.startY != it.endY }.forEach {
      var columns = it.columns().toList()
      var rows = it.rows().toList()
      if (it.startX > it.endX) {
        columns = columns.reversed()
      }
      if (it.startY > it.endY) {
        rows = rows.reversed()
      }
      check(columns.size == rows.size)
      for (i in columns.indices) {
        field[rows[i]][columns[i]]++
      }
    }
    return field.flatMap { it.asSequence() }.count { it >= 2 }
  }

  val input = readInput("Day05")
  val lines = input.map { line ->
    val startEnd = line.split(" -> ")
    check(startEnd.size == 2)
    val startXY = startEnd[0].split(",").map { it.toInt() }
    check(startXY.size == 2)
    val endXY = startEnd[1].split(",").map { it.toInt() }
    check(endXY.size == 2)
    Line(startXY[0], startXY[1], endXY[0], endXY[1])
  }
  println(part1(lines))
  println(part2(lines))
}

data class Line(val startX: Int, val startY: Int, val endX: Int, val endY: Int) {

  fun columns(): IntRange {
    if (startX > endX) {
      return endX..startX
    } else {
      return startX..endX
    }
  }

  fun rows(): IntRange {
    if (startY > endY) {
      return endY..startY
    }
    return startY..endY
  }
}