fun main() {
  fun part1(input: List<String>): Int {
    var x = 0
    var depth = 0
    input.forEach {
      val actionAndDistance = it.split(" ")
      assert(actionAndDistance.size == 2)
      if (actionAndDistance[0] == "forward") {
        x += actionAndDistance[1].toInt()
      } else if (actionAndDistance[0] == "down") {
        depth += actionAndDistance[1].toInt()
      } else if (actionAndDistance[0] == "up") {
        depth -= actionAndDistance[1].toInt()
      } else {
        throw Error("Unexpected action in $it")
      }
    }
    return x * depth
  }

  fun part2(input: List<String>): Int {
    var x = 0
    var depth = 0
    var aim = 0
    input.forEach {
      val actionAndDistance = it.split(" ")
      assert(actionAndDistance.size == 2)
      if (actionAndDistance[0] == "forward") {
        x += actionAndDistance[1].toInt()
        depth += aim * actionAndDistance[1].toInt()
      } else if (actionAndDistance[0] == "down") {
        aim += actionAndDistance[1].toInt()
      } else if (actionAndDistance[0] == "up") {
        aim -= actionAndDistance[1].toInt()
      } else {
        throw Error("Unexpected action in $it")
      }
    }
    return x * depth
  }

  val input = readInput("Day02")
  println(part1(input))
  println(part2(input))
}