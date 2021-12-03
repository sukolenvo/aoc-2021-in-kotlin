fun main() {
  fun part1(input: List<String>): Int {
    val numbers = input.map { it.toInt() }
    var prev = numbers[0]
    var result = 0
    for (i in 1 until numbers.size) {
      if (numbers[i] > prev) {
        result++
      }
      prev = numbers[i]
    }
    return result
  }

  fun part2(input: List<String>): Int {
    val numbers = input.map { it.toInt() }
    var result = 0
    for (i in 3 until numbers.size) {
      val slideOne = numbers[i - 3] + numbers[i - 2] + numbers[i - 1]
      val slideTwo = numbers[i - 2] + numbers[i - 1] + numbers[i]
      if (slideTwo > slideOne) {
        result ++
      }
    }
    return result
  }

  val input = readInput("Day01")
  println(part1(input))
  println(part2(input))
}