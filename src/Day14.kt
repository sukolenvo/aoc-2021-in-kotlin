object Day14 {

  val cache = mutableMapOf<String, Map<Char, Long>>()

  fun part1(template: String, mutations: Map<String, Char>): Long {
    return runMutations(template, 10, mutations)
  }

  fun runMutations(template: String, steps: Int, mutations: Map<String, Char>): Long {
    val counts = template.windowed(2).map { mutate(it[0], it[1], steps, mutations).toMutableMap() }.reduce { l, r ->
      r.forEach { (k, v) -> l.merge(k, v) { o, n -> o + n } }
      l
    }
    template.toCharArray().drop(1).dropLast(1).forEach {
      counts.compute(it) { _, v -> v!! - 1 }
    }
    return counts.values.maxOf { it } - counts.values.minOf { it }
  }

  fun mutate(left: Char, right: Char, steps: Int, mutations: Map<String, Char>): Map<Char, Long> {
    if (steps == 0) {
      if (left == right) {
        return mapOf(Pair(left, 2))
      }
      return mapOf(Pair(left, 1), Pair(right, 1))
    }
    val cacheKey = "$steps$left$right"
    if (cache.containsKey(cacheKey)) {
      return cache.getValue(cacheKey)
    }
    val insert = mutations.getValue("${left}${right}")
    val resultLeft = mutate(left, insert, steps - 1, mutations)
    val resulRight = mutate(insert, right, steps - 1, mutations)
    val result = resultLeft.toMutableMap()
    resulRight.forEach { (k, v) -> result.compute(k) { _, l -> (l ?: 0) + v } }
    result[insert] = result[insert]!! - 1
    cache[cacheKey] = result
    return result
  }

  fun part2(template: String, mutations: Map<String, Char>): Long {
    return runMutations(template, 40, mutations)
  }

  @JvmStatic
  fun main(args: Array<String>) {
    val input = readInput(javaClass.name)
    val mutations = input.drop(2).associate {
      val keyResult = it.split(" -> ")
      Pair(keyResult[0], keyResult[1][0])
    }
    println(part1(input[0], mutations))
    println(part2(input[0], mutations))
  }
}