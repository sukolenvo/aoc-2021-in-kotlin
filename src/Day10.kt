object Day10 {

  fun part1(input: List<String>): Long {
    return input.mapNotNull { analyseLine(it).first }.sumOf {
      when (it) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> throw Error("Invalid closing char")
      }.toLong()
    }
  }

  fun analyseLine(line: String): Pair<Char?, List<Char>?> {
    val pairs = mapOf(Pair('[', ']'), Pair('(', ')'), Pair('{', '}'), Pair('<', '>'))
    val stack = mutableListOf<Char>()
    for (i in line.indices) {
      val char = line[i]
      if (pairs.containsKey(char)) {
        stack.add(char)
      } else {
        val openChar = stack.removeLast()
        if (pairs[openChar] != char) {
          return Pair(char, null)
        }
      }
    }
    check(stack.isNotEmpty()) { "stack is empty" }
    return Pair(null, stack)
  }

  fun part2(input: List<String>): Long {
    val scores = input.mapNotNull { analyseLine(it).second }.map {
      var total: Long = 0
      for (c in it.reversed()) {
        total *= 5
        total += " ([{<".indexOf(c)
      }
      total
    }.sorted()
    check(scores.size % 2 == 1)
    return scores[scores.size / 2]
  }

  @JvmStatic
  fun main(args: Array<String>) {
    val input = readInput(javaClass.name)
    println(part1(input))
    println(part2(input))
  }
}
