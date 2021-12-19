object Day13 {

  fun part1(dots: Array<Array<Int>>, instructions: List<FoldInstruction>): Int {
    instructions.take(1).forEach { fold(dots, it) }
    return dots.sumOf { column -> column.count { it > 0 } }
  }

  private fun fold(dots: Array<Array<Int>>, instruction: FoldInstruction) {
    if (instruction.horizontal) {
      for (column in dots.indices) {
        for (row in 1 until (dots[0].size - instruction.length)) {
          if (instruction.length - row < 0) {
            break
          }
          dots[column][instruction.length - row] += dots[column][instruction.length + row]
          dots[column][instruction.length + row] = 0
        }
      }
    } else {
      for (column in 1 until (dots.size - instruction.length)) {
        if (instruction.length - column < 0) {
          break
        }
        for (row in dots[column].indices) {
          dots[instruction.length - column][row] += dots[instruction.length + column][row]
          dots[instruction.length + column][row] = 0
        }
      }
    }
  }

  fun part2(dots: Array<Array<Int>>, instructions: List<FoldInstruction>): Int {
    instructions.forEach { fold(dots, it) }
    for (y in 0..6) {
      for (x in 0..40) {
        if (dots[x][y] > 0) {
          print("#")
        } else {
          print(" ")
        }
      }
      print("\n")
    }
    return -1
  }

  @JvmStatic
  fun main(args: Array<String>) {
    val input = readInput(javaClass.name)
    val dots = input.takeWhile { it != "" }
    val foldInstructions = input.takeLastWhile { it != "" }
    check(dots.size + 1 + foldInstructions.size == input.size)
    val maxX = dots.map { it.split(",")[0] }.map { it.toInt() }.maxOf { it }
    val maxY = dots.map { it.split(",")[1] }.map { it.toInt() }.maxOf { it }
    val dotsMatrix = Array(maxX + 1) { Array(maxY + 1) { 0 } }
    dots.forEach {
      val xY = it.split(",")
      dotsMatrix[xY[0].toInt()][xY[1].toInt()]++
    }
    dotsMatrix.forEach { column -> check(column.none { it > 1 }) { "overlapping input" } }
    val instructions = foldInstructions.map { FoldInstruction.parseInstruction(it) }
    println(part1(dotsMatrix.map { it.clone() }.toTypedArray(), instructions))
    println(part2(dotsMatrix.map { it.clone() }.toTypedArray(), instructions))
  }
}

data class FoldInstruction(val horizontal: Boolean, val length: Int) {
  companion object Factory {
    fun parseInstruction(instruction: String): FoldInstruction {
      return if (instruction.startsWith("fold along x=")) {
        FoldInstruction(false, instruction.removePrefix("fold along x=").toInt())
      } else if (instruction.startsWith("fold along y=")) {
        FoldInstruction(true, instruction.removePrefix("fold along y=").toInt())
      } else {
        throw IllegalArgumentException("Invalid instruction: $instruction")
      }
    }
  }
}
