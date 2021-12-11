typealias Number = Long
fun main() {

  val cache = HashMap<Int, Number>()

  fun countFish(day: Int): Number {
    if (cache.contains(day)) {
      return cache[day]!!
    }
    var result: Number = 1
    for (i in 0..day step 7) {
      result += countFish(day - i - 9)
      check(result > 0) { "overflow" }
    }
    cache[day] = result
    return result
  }
  fun part1(input: List<Int>): Number {
    return input.map { countFish(80 - it - 1) }.sum()
  }


  fun part2(input: List<Int>): Number {
    return input.map { countFish(256 - it - 1) }.sum()
  }

  val input = readInput("Day06")
  val fishList = input[0].split(",").map { it.toInt() }
  println(part1(fishList))
  println(part2(fishList))
}